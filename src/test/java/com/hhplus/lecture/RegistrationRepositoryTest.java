package com.hhplus.lecture;


import com.hhplus.lecture.domain.entity.Registration;
import com.hhplus.lecture.domain.repository.RegistrationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;


@Transactional
@SpringBootTest
public class RegistrationRepositoryTest {
    @Autowired
    private RegistrationRepository registrationRepository;

    @BeforeEach
    void setUp(){
        Registration user1Registration1 = makeRegistration(1L, 1L);
        Registration user1Registration2 = makeRegistration(1L, 2L);
        Registration user1Registration3 = makeRegistration(1L, 3L);
        Registration user2Registration1 = makeRegistration(2L, 1L);
        Registration user2Registration2 = makeRegistration(2L, 2L);
        Registration user2Registration3 = makeRegistration(2L, 3L);
        registrationRepository.saveAll(List.of(user1Registration1, user1Registration2,
                user1Registration3, user2Registration1,
                user2Registration2,user2Registration3));
    }


    @Test
    @DisplayName("특정 UserId의 수강내역들을 조회한다.")
    void getUserRegistrationHisotoryByUserId(){
        List<Registration> user1Registrations = registrationRepository.findByUserId(2L);
        assertThat(user1Registrations).hasSize(3);
    }

    @Test
    @DisplayName("특정 UserId와 강의 번호로 수강내역을 조회한다.")
    void getUserRegistrationHistory(){
        Registration User1Registration = registrationRepository.findByUserIdAndLectureId(1L, 2L)
                .orElseThrow(()->new IllegalArgumentException("신청 이력이 없습니다."));
        assertThat(User1Registration.getLectureId()).isEqualTo(2L);
    }

    Registration makeRegistration(Long userId, Long lectureId) {
        return Registration.builder()
                                .userId(userId)
                                .lectureId(lectureId)
                            .build();
    }//makeRegistration

}
