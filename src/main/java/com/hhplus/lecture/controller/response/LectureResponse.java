package com.hhplus.lecture.controller.response;


import com.hhplus.lecture.domain.entity.Lecture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class LectureResponse {
    private String title;               // 강의명
    private String instructor;          // 강사명
    private LocalDateTime openDate;     // 강의 일시
    private Integer maxCapacity;        // 수강 가능인원
    private Integer currentCapacity;    // 현재 수강인원


    public LectureResponse(Lecture lecture) {
        this.title           = lecture.getTitle();
        this.instructor      = lecture.getInstructor();
        this.openDate        = lecture.getOpenDate();
        this.maxCapacity     = lecture.getMaxCapacity();
        this.currentCapacity = lecture.getCurrentCapacity();
    }


    public LectureResponse of(Lecture lecture){
        return new LectureResponse(lecture);
    }

    // Lecture 리스트를 LectureResponse 리스트로 변환하는 메서드
    public static List<LectureResponse> from(List<Lecture> lectures) {
        return lectures.stream()
                .map(LectureResponse::new)      // Lecture 객체를 LectureResponse로 변환
                .collect(Collectors.toList());  // 리스트로 수집
    }


}
