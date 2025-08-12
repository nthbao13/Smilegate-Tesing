package com.example.game.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        int status = 500;
        if (statusCode != null) {
            try {
                status = Integer.parseInt(statusCode.toString());
            } catch (NumberFormatException ignored) {}
        }

        String errorMsg;
        if (status == HttpStatus.NOT_FOUND.value()) {
            errorMsg = "The page you are looking for does not exist.";
        } else if (status == HttpStatus.FORBIDDEN.value()) {
            errorMsg = "You do not have permission to access this page.";
        } else {
            errorMsg = (message != null && !message.toString().isBlank())
                    ? message.toString()
                    : "An unexpected error occurred.";
        }

        model.addAttribute("errorCode", status);
        model.addAttribute("errorMessage", errorMsg);

        return "error_page";
    }

}
