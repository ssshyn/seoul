package com.sm.seoulmate.exception;

import lombok.Getter;

@Getter
public class ErrorException extends RuntimeException {
    private final ErrorCode errorCode;

    public ErrorException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
