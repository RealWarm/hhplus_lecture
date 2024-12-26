package com.hhplus.lecture.domain.repository;

import com.hhplus.lecture.domain.entity.Registration;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository {

    // 저장한다
    Registration save(Registration registration);
    List<Registration> saveAll(List<Registration> registration);

    // 특정 userId로 신청 완료된 특강 목록을 조회
    List<Registration> findByUserId(Long userId);

    // 특정 LectureId로 신청 완료된 특강 목록 조회
    List<Registration> findByLectureId(Long lectureId);

    // 특정 userId로 신청 완료된 특정 특강 조회
    Optional<Registration> findByUserIdAndLectureId(Long userId, Long lectureScheduleId);

}
