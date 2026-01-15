package com.bd.homepage.global.error;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ApiErrorResponse {
    private int status;
    private String code;       // 예: VALIDATION_ERROR
    private String message;    // 사용자용 메시지
    private LocalDateTime timestamp;
    private List<FieldErrorItem> errors;

    @Getter
    @Builder
    public static class FieldErrorItem {
        private String field;
        private String reason;
    }

    public static ApiErrorResponse of(int status, String code, String message) {
        return ApiErrorResponse.builder()
                .status(status)
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
