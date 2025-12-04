package com.erickson.spring_batch_demo.rest.controller;

import com.erickson.spring_batch_demo.exception.ImportJobException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
public class RestControllersAdvice {
    private static void logRequestThatCausedTheError(WebRequest request) {
        log.error(request.getDescription(false));
    }

    @ExceptionHandler(ImportJobException.class)
    protected ResponseEntity<Object> importJobExceptionHandler(ImportJobException importJobException,
                                                               WebRequest request) {
        logRequestThatCausedTheError(request);
        log.error("{}", importJobException.getApiError(), importJobException);

        var apiError = importJobException.getApiError();

        return new ResponseEntity<>(apiError.message(), apiError.httpStatus());
    }
}
