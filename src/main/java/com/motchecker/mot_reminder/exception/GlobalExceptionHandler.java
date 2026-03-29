package com.motchecker.mot_reminder.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUserAlreadyExists(UserAlreadyExistsException ex, Model model) {
        logger.warn("Handled UserAlreadyExistsException: {}", ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "error-page";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        logger.error("An unexpected error occurred: ", ex);
        model.addAttribute("errorMessage", "Oops! Something went wrong on our end. Please try again later.");
        return "error-page";
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public String handleResourceNotFound(NoResourceFoundException ex) {
        // 404 is error
        // BUUUUT only silent one !
        logger.warn("Resource not found (404): " + ex.getResourcePath());

        // route to error page (or bring back 404)
        return "error-page";
    }
}