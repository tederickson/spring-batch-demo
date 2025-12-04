package com.erickson.spring_batch_demo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ImportJobException extends Exception {
    final ApiError apiError;

    public ImportJobException(ApiError apiError) {
        super(apiError.message());

        this.apiError = apiError;
    }

    public ImportJobException(String message, HttpStatus httpStatus) {
        this(new ApiError(message, httpStatus));
    }
}
