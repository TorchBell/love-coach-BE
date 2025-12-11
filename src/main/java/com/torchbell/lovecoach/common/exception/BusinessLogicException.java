package com.torchbell.lovecoach.common.exception;

import com.torchbell.lovecoach.common.response.ResponseCode;
import lombok.Getter;

@Getter
public class BusinessLogicException extends RuntimeException{

    private final ResponseCode responseCode;

    public BusinessLogicException(ResponseCode responseCode){
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }

    public BusinessLogicException(ResponseCode responseCode, String customMessage){
        super(customMessage);
        this.responseCode = responseCode;
    }
}
