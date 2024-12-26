package com.hhplus.lecture.domain.service;


import com.hhplus.lecture.controller.request.LectureApplyRequest;
import com.hhplus.lecture.controller.response.LectureResponse;
import com.hhplus.lecture.controller.response.RegistrationResponse;
import com.hhplus.lecture.domain.entity.Lecture;
import com.hhplus.lecture.domain.entity.Registration;
import com.hhplus.lecture.domain.exception.AlreadyApplyLectureException;
import com.hhplus.lecture.domain.exception.LectureApplyLimitFullException;
import com.hhplus.lecture.domain.exception.OutOfDateException;
import com.hhplus.lecture.domain.repository.LectureRepository;
import com.hhplus.lecture.domain.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;
    private final RegistrationRepository registrationRepository;


    public List<LectureResponse> findRegistableLectures(LocalDateTime now) {
        List<Lecture> availableLectures = lectureRepository.findAvailableLecture(now);
        return LectureResponse.from(availableLectures);
    }//findRegistableLectures


    public ResponseEntity<String> registLecture(LectureApplyRequest request)
            throws LectureApplyLimitFullException, AlreadyApplyLectureException, OutOfDateException {
        Long lectureId = request.getLectureId();
        Long userId = request.getUserId();

        // 강의가 존재하니?
        Lecture lecture = lectureRepository.findById(lectureId)
                                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강의 입니다."));

        // 수강기간이 지난 강의는 신청하지 못한다.
        lecture.isApplicationPeriod(LocalDateTime.now());

        // 공석이 존재하면 현재 수강인원 1증가, 공석이 없으면 LectureApplyLimitFullException 발생
        lecture.addCurrentCapacity();

        // 현재유저가 신청한 이력이있니?
        Optional<Registration> registration = registrationRepository.findByUserIdAndLectureId(userId, lectureId);
        if(registration.isPresent()){
            throw new AlreadyApplyLectureException("이미 신청한 이력이 있습니다.");
        }//if

        // 신청내역을 등록한다.  register
        registrationRepository.save(Registration.builder()
                                                    .lectureId(lectureId)
                                                    .userId(userId)
                                                .build());
        return ResponseEntity.ok().body("수강이 완료 되었습니다.");
    }//registLecture



    public List<RegistrationResponse> getUserRegistrationHistory(Long userId) {
        List<Registration> registrations = registrationRepository.findByUserId(userId);
        return RegistrationResponse.from(registrations);
    }//getUserRegistrationHistory


}//end
