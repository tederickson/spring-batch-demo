package com.erickson.spring_batch_demo.exception;

import org.springframework.http.HttpStatus;


public record ApiError(String message, HttpStatus httpStatus) {
}
