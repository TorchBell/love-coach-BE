package com.torchbell.lovecoach.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * API 공통 응답 객체
 * API 명세서와 동일한 { status, message, data } 구조를 가집니다.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {

    private final int status;
    private final String message;
    private final T data;

    // 1. 성공 응답 (데이터 있음)
    public static <T> CommonResponse<T> ofSuccess(T data) {
        return new CommonResponse<>(
                ResponseCode.SUCCESS.getHttpStatus().value(),
                ResponseCode.SUCCESS.getMessage(),
                data
        );
    }

    // 2. 성공 응답 (데이터 없음)
    public static CommonResponse<Void> ofSuccess() {
        return new CommonResponse<>(
                ResponseCode.SUCCESS.getHttpStatus().value(),
                ResponseCode.SUCCESS.getMessage(),
                null
        );
    }

    // 3. 실패 응답 (ResponseCode 사용)
    public static CommonResponse<Void> ofFailure(ResponseCode code) {
        return new CommonResponse<>(
                code.getHttpStatus().value(),
                code.getMessage(),
                null
        );
    }

    // 4. 실패 응답 (ResponseCode + 커스텀 메시지)
    public static CommonResponse<Void> ofFailure(ResponseCode code, String customMessage) {
        return new CommonResponse<>(
                code.getHttpStatus().value(),
                customMessage,
                null
        );
    }
}