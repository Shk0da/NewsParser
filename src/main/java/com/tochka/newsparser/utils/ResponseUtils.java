package com.tochka.newsparser.utils;

import com.tochka.newsparser.controller.rest.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtils {

    public static <S> ResponseEntity<?> response(S obj) {
        return (obj != null)
                ? new ResponseEntity<>(obj, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
