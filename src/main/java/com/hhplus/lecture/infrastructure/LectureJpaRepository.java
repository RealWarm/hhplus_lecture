package com.hhplus.lecture.infrastructure;

import com.hhplus.lecture.domain.entity.Lecture;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface LectureJpaRepository extends JpaRepository<Lecture, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Lecture> findById(Long id);

    Optional<Lecture> findByTitleAndInstructor(String title, String instructor);

    @Query("SELECT l FROM Lecture l WHERE l.openDate > :currentTime AND l.currentCapacity<30")
    List<Lecture> findAvailableLecture(@Param("currentTime") LocalDateTime currentTime);

}
