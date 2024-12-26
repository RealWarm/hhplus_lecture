package com.hhplus.lecture.common;

import com.hhplus.lecture.domain.exception.AlreadyApplyLectureException;
import com.hhplus.lecture.domain.exception.LectureApplyLimitFullException;
import com.hhplus.lecture.domain.exception.OutOfDateException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
class ApiControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(500).body(new ErrorResponse("400", e.getMessage()));
    }

    @ExceptionHandler(value = AlreadyApplyLectureException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyApplyLectureException(AlreadyApplyLectureException e) {
        return ResponseEntity.status(500).body(new ErrorResponse("409", e.getMessage()));
    }

    @ExceptionHandler(value = LectureApplyLimitFullException.class)
    public ResponseEntity<ErrorResponse> handleLectureApplyLimitFullException(LectureApplyLimitFullException e) {
        return ResponseEntity.status(500).body(new ErrorResponse("409", e.getMessage()));
    }

    @ExceptionHandler(value = OutOfDateException.class)
    public ResponseEntity<ErrorResponse> handleOutOfDateException(LectureApplyLimitFullException e) {
        return ResponseEntity.status(500).body(new ErrorResponse("400", e.getMessage()));
    }

}//end
