package com.tochka.newsparser.controller.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ApiError {

    private Integer status;
    private String message;
    private String error;

    public ApiError(HttpStatus status, String message, String error) {
        this.status = status.value();
        this.message = message;
        this.error = error;
    }
}