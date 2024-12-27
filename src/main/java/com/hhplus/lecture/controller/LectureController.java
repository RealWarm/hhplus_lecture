package com.hhplus.lecture.controller;


import com.hhplus.lecture.controller.request.LectureApplyRequest;
import com.hhplus.lecture.controller.response.LectureResponse;
import com.hhplus.lecture.controller.response.RegistrationResponse;
import com.hhplus.lecture.domain.exception.AlreadyApplyLectureException;
import com.hhplus.lecture.domain.exception.LectureApplyLimitFullException;
import com.hhplus.lecture.domain.exception.OutOfDateException;
import com.hhplus.lecture.domain.service.LectureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/lectures")
public class LectureController {
    private final LectureService lectureService;


    /**
     * 특강 신청 가능 목록 API
     */
    @GetMapping
    public ResponseEntity<List<LectureResponse>> getLectures() {
        return ResponseEntity.ok().body(lectureService.findRegistableLectures(LocalDateTime.now()));
    }//getLectures


    /**
     * 특강 신청 API
     */
    @PostMapping("/apply")
    public ResponseEntity<String> applyLecture(
            @RequestBody LectureApplyRequest request
    ) throws LectureApplyLimitFullException, AlreadyApplyLectureException, OutOfDateException {
        return lectureService.registLecture(request);
    }//applyLecture


    /**
     * 특강 신청 완료 목록 조회 API
     */
    @GetMapping("/registrations/{userId}")
    public ResponseEntity<List<RegistrationResponse>> getUserRegistrations(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok().body(lectureService.getUserRegistrationHistory(userId));
    }//getUserRegistrations


}//end
