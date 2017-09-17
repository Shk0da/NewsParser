package com.tochka.newsparser.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {

    public static class ErrorWithMessage extends RuntimeException {
        public ErrorWithMessage(String message) {
            super(message);
        }
    }

    public static class NotFoundWithMessage extends RuntimeException {
        public NotFoundWithMessage(String message) {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handle500(ErrorWithMessage ex, WebRequest request) throws Exception {
        return new ResponseEntity<>(
                new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), ""), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler({ ErrorWithMessage.class })
    public ResponseEntity<?> handleErrorWithMessage(ErrorWithMessage ex, WebRequest request) {
        return new ResponseEntity<>(
                new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ""), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({ NotFoundWithMessage.class })
    public ResponseEntity<?> handleNotFoundWithMessage(NotFoundWithMessage ex, WebRequest request) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
