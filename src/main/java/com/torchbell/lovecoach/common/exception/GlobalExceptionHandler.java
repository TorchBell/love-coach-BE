package com.torchbell.lovecoach.common.exception;

import com.torchbell.lovecoach.common.response.CommonResponse;
import com.torchbell.lovecoach.common.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비즈니스 로직 예외 처리
     */
    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<CommonResponse<Void>> handleBusinessLogicException(BusinessLogicException e) {
        log.warn("BusinessLogicException [{}]: {}", e.getResponseCode(), e.getMessage());

        // CommonResponse.error()를 사용하여 명세서 구조 유지
        return ResponseEntity
                .status(e.getResponseCode().getHttpStatus())
                .body(CommonResponse.ofFailure(e.getResponseCode(), e.getMessage()));
    }

    /**
     * 잘못된 요청 파라미터 예외 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("IllegalArgumentException: {}", e.getMessage());

        return ResponseEntity
                .status(ResponseCode.BAD_REQUEST.getHttpStatus())
                .body(CommonResponse.ofFailure(ResponseCode.BAD_REQUEST, e.getMessage()));
    }

    /**
     * 기타 예상치 못한 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Void>> handleException(Exception e) {
        log.error("Unhandled Exception: ", e);

        return ResponseEntity
                .status(ResponseCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(CommonResponse.ofFailure(ResponseCode.INTERNAL_SERVER_ERROR));
    }
}