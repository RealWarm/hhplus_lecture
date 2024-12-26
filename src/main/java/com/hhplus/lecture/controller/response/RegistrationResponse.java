package com.hhplus.lecture.controller.response;

import com.hhplus.lecture.domain.entity.Registration;

import java.util.List;
import java.util.stream.Collectors;


public class RegistrationResponse {
    private Long userId;
    private Long lectureId;

    public RegistrationResponse(Registration registration) {
        this.userId = registration.getUserId();
        this.lectureId = registration.getLectureId();
    }

    // Registration 리스트를 RegistrationResponse 리스트로 변환하는 메서드
    public static List<RegistrationResponse> from(List<Registration> registrations) {
        return registrations.stream()
                .map(RegistrationResponse::new)      // Lecture 객체를 LectureResponse로 변환
                .collect(Collectors.toList());  // 리스트로 수집
    }
}
