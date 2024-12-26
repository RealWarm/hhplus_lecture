package com.hhplus.lecture.common;

public record ErrorResponse(
        String code,
        String message
) {
}
