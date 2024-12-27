package com.hhplus.lecture.infrastructure;

import com.hhplus.lecture.domain.entity.Registration;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RegistrationJpaRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByUserId(Long userId);

    List<Registration> findByLectureId(Long userId);

    Optional<Registration> findByUserIdAndLectureId(Long userId, Long lectureScheduleId);
}
