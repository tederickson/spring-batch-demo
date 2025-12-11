package com.erickson.spring_batch_demo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ImportJobException extends Exception {
    private final ApiError apiError;

    public ImportJobException(String message, HttpStatus httpStatus) {
        super(message);
        apiError = new ApiError(message, httpStatus);
    }
}
