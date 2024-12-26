package com.hhplus.lecture;

import com.hhplus.lecture.controller.request.LectureApplyRequest;
import com.hhplus.lecture.domain.entity.Lecture;
import com.hhplus.lecture.domain.entity.Registration;
import com.hhplus.lecture.domain.exception.AlreadyApplyLectureException;
import com.hhplus.lecture.domain.exception.LectureApplyLimitFullException;
import com.hhplus.lecture.domain.exception.OutOfDateException;
import com.hhplus.lecture.domain.repository.LectureRepository;
import com.hhplus.lecture.domain.repository.RegistrationRepository;
import com.hhplus.lecture.domain.service.LectureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class LectureServiceUnitTest {

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private RegistrationRepository registrationRepository;

    @InjectMocks
    private LectureService lectureService;

    private Lecture lecture;
    private LectureApplyRequest request;
    private Long userId = 1L;
    private Long lectureId = 1L;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        lecture = Lecture.builder()
                .title("렌의 TDD 강의 1편")
                .instructor("렌 코치님")
                .openDate(LocalDateTime.now().plusDays(1))
                .maxCapacity(30)
                .currentCapacity(20)
                .build();

        request = new LectureApplyRequest(userId, lectureId);
    }//setUp


    @Test
    @DisplayName("강의 신청에 성공한다.")
    public void registLecture_Success() throws Exception {
        // given
        when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(lecture));
        when(registrationRepository.findByUserIdAndLectureId(userId, lectureId)).thenReturn(Optional.empty()); // 신청이력이 없음

        // when
        ResponseEntity<String> response = lectureService.registLecture(request);

        // then
        verify(registrationRepository).save(any(Registration.class)); // save 메서드 호출 확인
        assertThat(lecture.getCurrentCapacity()).isEqualTo(21); // 현재 수강인원 증가 확인
        assertThat(response.getBody()).isEqualTo("수강이 완료 되었습니다."); // 함수 호출 후 Response 확인
    }//registLecture_Success


    @Test
    @DisplayName("등록되지 않은 강의를 조회하면 에러 발생")
    public void Lecture_NotFound() {
        // given
        when(lectureRepository.findById(lectureId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->  lectureService.registLecture(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 강의 입니다.");
    }//LectureNotFound


    @Test
    @DisplayName("신청기간이 지난 강의를 신청하려면 에러 발생")
    public void regist_outOfDate_lectures(){
        // given
        Lecture outOflecture = Lecture.builder()
                .title("렌의 TDD 강의 1편")
                .instructor("렌 코치님")
                .openDate(LocalDateTime.now().minusSeconds(1))
                .maxCapacity(30)
                .currentCapacity(20)
                .build();
        when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(outOflecture)); // 신청기간이 지난 강의 등록
        when(registrationRepository.findByUserIdAndLectureId(userId, lectureId)).thenReturn(Optional.empty()); // 신청이력이 없음

        // when & then
        assertThatThrownBy(() ->  lectureService.registLecture(request))
                .isInstanceOf(OutOfDateException.class)
                .hasMessage("수강 신청 기간이 지났습니다.");
        verify(registrationRepository, never()).save(any(Registration.class)); // 당연히 수강이력은 등록하지 않는다.
    }//regist_outOfDate_lectures


    @Test
    @DisplayName("이미 신청한 이력이 있는 강의를 신청하면 에러발생")
    public void regist_AlreadyApplied_Lecture() {
        // given
        when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(lecture));
        when(registrationRepository.findByUserIdAndLectureId(userId, lectureId))
                                        .thenReturn(Optional.of(new Registration(userId, lectureId)));

        // when & then
        assertThatThrownBy(() ->  lectureService.registLecture(request))
                .isInstanceOf(AlreadyApplyLectureException.class)
                .hasMessage("이미 신청한 이력이 있습니다.");
    }//regist_AlreadyApplied_Lecture


    @Test
    @DisplayName("만석인 강의를 신청하려면 에러발생")
    public void regist_CapacityFull_Lecture() {
        // given
        Lecture fullLecture = Lecture.builder()
                .title("렌의 TDD 강의 1편")
                .instructor("렌 코치님")
                .openDate(LocalDateTime.now().plusDays(1))
                .maxCapacity(30)
                .currentCapacity(30)
                .build();
        when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(fullLecture));
        when(registrationRepository.findByUserIdAndLectureId(userId, lectureId)).thenReturn(Optional.empty()); // 신청이력 없음

        // when & then
        assertThatThrownBy(() ->  lectureService.registLecture(request))
                .isInstanceOf(LectureApplyLimitFullException.class)
                .hasMessage("공석이 없습니다.");
    }//regist_CapacityFull_Lecture

}//end