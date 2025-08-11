package com.example.game.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    GAME_CODE_EXISTS(1000, "This game is existed", HttpStatus.BAD_REQUEST),
    GAME_LANGUAGE_ERROR(1001, "This language is not correct", HttpStatus.BAD_REQUEST),
    GAME_CATEGORY_EXISTS(1002, "This category is existed", HttpStatus.BAD_REQUEST),
    INVALID_CATEGORY_NAME(1003, "Invalid category name", HttpStatus.BAD_REQUEST),
    NOT_FOUND(1004, "Not found", HttpStatus.NOT_FOUND)
    ;

    private int code;
    private String message;
    private HttpStatus statusCode;
    private String viewName;

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
