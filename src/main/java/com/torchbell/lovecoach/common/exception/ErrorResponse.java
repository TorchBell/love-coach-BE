package com.torchbell.lovecoach.common.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    private String status;
    private String message;

    public static ErrorResponse of(String status, String message) {
        return ErrorResponse.builder()
                .status(status)
                .message(message)
                .build();
    }
}
