package com.hhplus.lecture;


import com.hhplus.lecture.controller.request.LectureApplyRequest;
import com.hhplus.lecture.domain.entity.Lecture;
import com.hhplus.lecture.domain.entity.Registration;
import com.hhplus.lecture.domain.exception.AlreadyApplyLectureException;
import com.hhplus.lecture.domain.repository.LectureRepository;
import com.hhplus.lecture.domain.repository.RegistrationRepository;
import com.hhplus.lecture.domain.service.LectureService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@SpringBootTest
public class LectureServiceTest {

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private LectureService lectureService;

    private Long userId = 11L;
    private Long lectureId;
    private Lecture lecture;

    @BeforeEach
    public void setUp() {
        lecture = Lecture.builder()
                                .title("렌의 TDD 강의 1편")
                                .instructor("렌 코치님")
                                .openDate(LocalDateTime.now().plusDays(1))
                                .currentCapacity(0)
                                .maxCapacity(30)
                            .build();
        Lecture saved = lectureRepository.save(lecture);
        lectureId=lecture.getId();
    }//setUp


    @Test
    @DisplayName("강의 신청에 성공한다.")
    public void registLecture_Success() throws Exception {
        // given
        LectureApplyRequest request = new LectureApplyRequest(userId, lectureId);

        // when
        ResponseEntity<String> response = lectureService.registLecture(request);

        List<Registration> result = registrationRepository.findByLectureId(lectureId);
        Lecture lecture1 = lectureRepository.findByTitleAndInstructor("렌의 TDD 강의 1편", "렌 코치님").orElseThrow(() -> new RuntimeException("에러"));


        assertThat(result).hasSize(1);
        assertThat(lecture1.getCurrentCapacity()).isEqualTo(1);
        assertThat(result.get(0).getUserId()).isEqualTo(11L);
    }//registLecture_Success

    @Test
    @DisplayName("동일한 유저 정보로 같은 특강을 5번 신청했을 때 1번만 성공")
    public void registJustOneTimeAvailable() {
        int userTry = 5;
        int failCnt = 0;
        // given
        LectureApplyRequest request = new LectureApplyRequest(userId, lectureId);

        // when
        for (int i = 0; i < userTry; i++) {
            try {
                lectureService.registLecture(request);
            } catch (Exception e) {
                failCnt++;
            }//try
        }//for-i


        List<Registration> result = registrationRepository.findByLectureId(lectureId);

        assertThat(result).hasSize(1);
        assertThat(failCnt).isEqualTo(4);
    }//registJustOneTimeAvailable


    @Test
    @DisplayName("동일한 유저 정보로 같은 특강을 동시에 5번 신청했을 때 1번만 성공")
    public void registJustOneTimeAvailable2() throws InterruptedException {
        // given
        int threadCnt = 5;
        int expectedSuccessCnt = 1;
        int expectedFailCnt = 4;
        CountDownLatch latch = new CountDownLatch(threadCnt);
        ExecutorService executorService = Executors.newFixedThreadPool(threadCnt);
        AtomicInteger successCnt = new AtomicInteger();
        AtomicInteger failCnt = new AtomicInteger();

        // when
        for (int i = 0; i < threadCnt; i++) {
            executorService.execute(() -> {
                try {
                    Long userId = 2L;
                    LectureApplyRequest request = new LectureApplyRequest(userId, lectureId);
                    lectureService.registLecture(request);

                    successCnt.getAndIncrement();
                }catch (Exception e) {
                    failCnt.getAndIncrement();
                } finally {
                    latch.countDown();
                }//try
            });
        }//for-i

        latch.await();
        executorService.shutdown();

        Lecture testedLecture = lectureRepository.findById(lectureId).get();
        List<Registration> registrations = registrationRepository.findByLectureId(lectureId);

        assertThat(registrations).hasSize(expectedSuccessCnt);
        assertThat(testedLecture.getCurrentCapacity()).isEqualTo(expectedSuccessCnt);
        assertThat(successCnt.get()).isEqualTo(expectedSuccessCnt);
        assertThat(failCnt.get()).isEqualTo(expectedFailCnt);
    }//registJustOneTimeAvailable


    @Test
    @DisplayName("수강 인원이 30명인 특강을 동시에 40명의 유저가 수강 신청하면 딱 30명만 수강 신청 완료가 된다.")
    void LectureApplyWhenConcurrency() throws InterruptedException {
        // given
        int threadCnt = 40;
        int expectedSuccessCnt = 30;
        int expectedFailCnt = 10;
        CountDownLatch latch = new CountDownLatch(threadCnt);
        ExecutorService executorService = Executors.newFixedThreadPool(threadCnt);
        AtomicInteger successCnt = new AtomicInteger();
        AtomicInteger failCnt = new AtomicInteger();

        // when
        for (int i = 0; i < threadCnt; i++) {
            executorService.execute(() -> {
                try {
                    Long userId = ThreadLocalRandom.current().nextLong(1, 10_000_000);
                    LectureApplyRequest request = new LectureApplyRequest(userId, lectureId);
                    lectureService.registLecture(request);

                    successCnt.getAndIncrement();
                } catch (Exception e) {
                    failCnt.getAndIncrement();
                } finally {
                    latch.countDown();
                }//try
            });
        }//for-i

        latch.await();
        executorService.shutdown();

        Lecture testedLecture = lectureRepository.findById(lectureId).get();
        List<Registration> registrations = registrationRepository.findByLectureId(lectureId);

        assertThat(registrations).hasSize(expectedSuccessCnt);
        assertThat(testedLecture.getCurrentCapacity()).isEqualTo(expectedSuccessCnt);
        assertThat(successCnt.get()).isEqualTo(expectedSuccessCnt);
        assertThat(failCnt.get()).isEqualTo(expectedFailCnt);
    }//LectureApplyWhenConcurrency


}//end