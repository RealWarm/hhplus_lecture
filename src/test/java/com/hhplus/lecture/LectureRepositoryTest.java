package com.hhplus.lecture;

import com.hhplus.lecture.domain.entity.Lecture;
import com.hhplus.lecture.domain.exception.LectureApplyLimitFullException;
import com.hhplus.lecture.domain.repository.LectureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@Transactional
@SpringBootTest
class LectureRepositoryTest {
    @Autowired
    LectureRepository lectureRepository;


    @BeforeEach
    public void setUp() {
        Lecture lecture1 = makeLecture("렌의 TDD 강의 1편", "렌 코치님", LocalDateTime.now().plusDays(1), 30, 29);
        Lecture lecture2 = makeLecture("렌의 TDD 강의 2편", "렌 코치님", LocalDateTime.now().plusDays(1), 30, 29);
        Lecture lecture3 = makeLecture("렌의 TDD 강의 3편", "렌 코치님", LocalDateTime.now().minusDays(1), 30, 29);
        Lecture lecture4 = makeLecture("허재의 TDD 강의 1편", "허재 코치님", LocalDateTime.now().plusDays(1), 30, 29);
        Lecture lecture5 = makeLecture("허재의 TDD 강의 2편", "허재 코치님", LocalDateTime.now().plusDays(1), 30, 29);
        Lecture lecture6 = makeLecture("허재의 TDD 강의 3편", "허재 코치님", LocalDateTime.now().minusDays(1), 30, 29);

        lectureRepository.saveAll(List.of(lecture1, lecture2, lecture3, lecture4, lecture5, lecture6));
    }//setUp


    // 전체 테스트로 실행하면 왜 실패지? 단일은 성공하는데
    @Test
    @DisplayName("강의 id로 조회한다.")
    void findLectureById_Test() {
        // when & then
        Lecture lecture = lectureRepository.findById(1L).orElseThrow(() -> new RuntimeException("없는 강의 입니다."));
        assertThat(lecture.getId()).isEqualTo(1L);
    }


    @Test
    @DisplayName("강의를 강의명과 강사명으로 조회한다")
    void findLectureByLectureNameAndInstructor() {
        // when & then
        Lecture lecture = lectureRepository.findByTitleAndInstructor("렌의 TDD 강의 2편", "렌 코치님").orElseThrow(() -> new RuntimeException("없는 강의 입니다."));
        assertThat(lecture.getTitle()).isEqualTo("렌의 TDD 강의 2편");
        assertThat(lecture.getInstructor()).isEqualTo("렌 코치님");
    }


    @Test
    @DisplayName("현재 강의 인원을 +1 한다. (DirtyChecking으로 업데이트를 진행한다.)")
    void addCurrentNumber() throws LectureApplyLimitFullException {
        // given
        Lecture lecture = lectureRepository.findByTitleAndInstructor("렌의 TDD 강의 2편", "렌 코치님").orElseThrow(() -> new RuntimeException("없는 강의 입니다."));
        Integer nowNuber = lecture.getCurrentCapacity();

        // when
        lecture.addCurrentCapacity();

        // then
        Lecture updatedLecture = lectureRepository.findByTitleAndInstructor("렌의 TDD 강의 2편", "렌 코치님").orElseThrow(() -> new RuntimeException("없는 강의 입니다."));
        Integer updatedNumber = updatedLecture.getCurrentCapacity();
        assertThat(nowNuber + 1).isEqualTo(updatedNumber);
    }


    @Test
    @DisplayName("자리가 부족한 경우 에러를 발생한다.")
    void throwErrorWhenHasNoSeatInLecture() throws LectureApplyLimitFullException {
        // given
        Lecture lecture = lectureRepository.findByTitleAndInstructor("렌의 TDD 강의 2편", "렌 코치님").orElseThrow(() -> new RuntimeException("없는 강의 입니다."));
        lecture.addCurrentCapacity();

        // when & then
        assertThatThrownBy(() -> lecture.addCurrentCapacity())
                .isInstanceOf(LectureApplyLimitFullException.class)
                .hasMessage("공석이 없습니다.");
    }


    @Test
    @DisplayName("강의 일시가 현재일을 지나지 않았고, 아직 인원이 30미만인 것들을 조회")
    void findAvailabeLecture() {

        List<Lecture> availableLecture = lectureRepository.findAvailableLecture(LocalDateTime.now());

        assertThat(availableLecture).hasSize(4)
                .extracting("title", "instructor")
                .containsExactlyInAnyOrder(
                        tuple("렌의 TDD 강의 1편", "렌 코치님"),
                        tuple("렌의 TDD 강의 2편", "렌 코치님"),
                        tuple("허재의 TDD 강의 1편", "허재 코치님"),
                        tuple("허재의 TDD 강의 2편", "허재 코치님")
                );
    }//findTest


    @Test
    @DisplayName("강의 일시가 지난것들은 검색되지 않는다.")
    void findUnAvailabeLecture() {

        List<Lecture> availableLecture = lectureRepository.findAvailableLecture(LocalDateTime.now());

        Lecture lecture3 = makeLecture("렌의 TDD 강의 3편", "렌 코치님", LocalDateTime.now().minusDays(1), 30, 11);
        assertThat(availableLecture).isNotIn(lecture3);
    }//findTest


    public Lecture makeLecture(String title,
                               String instructor,
                               LocalDateTime openDate,
                               Integer maxCapacity,
                               Integer currentCapacity) {
        return Lecture.builder()
                .title(title)
                .instructor(instructor)
                .openDate(openDate)
                .maxCapacity(maxCapacity)
                .currentCapacity(currentCapacity)
                .build();
    }//makeLecture


}//end
