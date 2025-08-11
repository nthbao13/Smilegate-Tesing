package com.example.game.exception;

public class CustomException extends RuntimeException{

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    private ErrorCode errorCode;

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
