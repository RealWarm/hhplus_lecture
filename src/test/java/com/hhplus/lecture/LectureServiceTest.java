package com.hhplus.lecture;


import com.hhplus.lecture.controller.request.LectureApplyRequest;
import com.hhplus.lecture.domain.entity.Lecture;
import com.hhplus.lecture.domain.entity.Registration;
import com.hhplus.lecture.domain.repository.LectureRepository;
import com.hhplus.lecture.domain.repository.RegistrationRepository;
import com.hhplus.lecture.domain.service.LectureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest
public class LectureServiceTest {

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private LectureService lectureService;

    private Long userId = 11L;
    private Long lectureId = 1L;

    @BeforeEach
    public void setUp() {


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
                    LectureApplyRequest request = new LectureApplyRequest(userId, 2L);
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

        assertThat(registrations).hasSize(30);
        assertThat(testedLecture.getCurrentCapacity()).isEqualTo(expectedSuccessCnt);
        assertThat(successCnt.get()).isEqualTo(expectedSuccessCnt);
        assertThat(failCnt.get()).isEqualTo(expectedFailCnt);
    }//LectureApplyWhenConcurrency


}//end