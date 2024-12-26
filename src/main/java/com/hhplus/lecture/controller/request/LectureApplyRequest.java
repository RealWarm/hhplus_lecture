package com.hhplus.lecture.controller.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class LectureApplyRequest {
    private Long userId;
    private Long lectureId;

    public LectureApplyRequest(Long userId, Long lectureId) {
        this.userId = userId;
        this.lectureId = lectureId;
    }// init

}// end
