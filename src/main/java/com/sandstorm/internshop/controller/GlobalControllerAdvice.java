package com.sandstorm.internshop.controller;

import com.sandstorm.internshop.exception.CustomerNotFound;
import com.sandstorm.internshop.exception.NotFound;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler
    public void handleNotFoundException(NotFound exception, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }


}
