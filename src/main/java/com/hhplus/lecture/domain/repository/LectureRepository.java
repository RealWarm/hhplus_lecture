package com.hhplus.lecture.domain.repository;

import com.hhplus.lecture.domain.entity.Lecture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LectureRepository {

    // 강의를 등록한다.
    Lecture save(Lecture lecture);

    List<Lecture> saveAll(List<Lecture> lectures);

    // 강의를 id로 조회한다.
    Optional<Lecture> findById(Long id);

    // 강의를 강의명과 강사명으로 조회한다.
    Optional<Lecture> findByTitleAndInstructor(String title, String instructor);

    // 강의의 현재 인원을 +1 증가시킨다.
    Lecture update(Lecture lecture);

    // 강의 일시가 현재일을 지나지 않았고, 아직 인원이 30미만인 것들을 조회
    List<Lecture> findAvailableLecture(LocalDateTime currentTime);


}
