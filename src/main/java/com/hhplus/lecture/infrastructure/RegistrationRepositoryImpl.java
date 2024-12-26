package com.hhplus.lecture.infrastructure;


import com.hhplus.lecture.domain.entity.Registration;
import com.hhplus.lecture.domain.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RegistrationRepositoryImpl implements RegistrationRepository {
    private final RegistrationJpaRepository registrationJpaRepository;

    @Override
    public Registration save(Registration registration) {
        return registrationJpaRepository.save(registration);
    }


    @Override
    public List<Registration> saveAll(List<Registration> registrations) {
        return registrationJpaRepository.saveAll(registrations);
    }


    @Override
    public List<Registration> findByUserId(Long userId) {
        return registrationJpaRepository.findByUserId(userId);
    }


    @Override
    public List<Registration> findByLectureId(Long lectureId) {
        return registrationJpaRepository.findByLectureId(lectureId);
    }


    @Override
    public Optional<Registration> findByUserIdAndLectureId(Long userId, Long lectureScheduleId) {
        return registrationJpaRepository.findByUserIdAndLectureId(userId, lectureScheduleId);
    }

}
