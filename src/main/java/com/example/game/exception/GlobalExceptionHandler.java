package com.example.game.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ModelAndView handleCustomException(Exception e, HttpServletRequest request) {

        return new ModelAndView("error_page")
                .addObject("errorCode", 500)
                .addObject("errorMessage", e.getMessage());

    }
}
