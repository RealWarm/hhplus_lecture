package com.hhplus.lecture.infrastructure;

import com.hhplus.lecture.domain.entity.Lecture;
import com.hhplus.lecture.domain.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureRepository {
    private final LectureJpaRepository lectureJpaRepository;


    @Override
    public Lecture save(Lecture lecture) {
        return lectureJpaRepository.save(lecture);
    }//save

    @Override
    public List<Lecture> saveAll(List<Lecture> lectures) {
        return lectureJpaRepository.saveAll(lectures);
    }

    @Override
    public Optional<Lecture> findById(Long id) {
        return lectureJpaRepository.findById(id);
    }//findById

    @Override
    public Optional<Lecture> findByTitleAndInstructor(String title, String instructor) {
        return lectureJpaRepository.findByTitleAndInstructor(title, instructor);
    }

    @Override
    public Lecture update(Lecture lecture) {
        return lectureJpaRepository.save(lecture);
    }

    @Override
    public List<Lecture> findAvailableLecture(LocalDateTime currentTime) {
        return lectureJpaRepository.findAvailableLecture(currentTime);
    }

}//end