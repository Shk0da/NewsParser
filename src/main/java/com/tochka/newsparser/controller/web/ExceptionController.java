package com.tochka.newsparser.controller.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionController {

    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest request, Exception e) {
        logger.error("Request: " + request.getRequestURL() + " raised " + e);
        return new ModelAndView("errors/500");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleError403(HttpServletRequest request, Exception e) {
        logger.error("Request: " + request.getRequestURL() + " raised " + e);
        return new ModelAndView("errors/403");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleError404(HttpServletRequest request, Exception e) {
        logger.error("Request: " + request.getRequestURL() + " raised " + e);
        return new ModelAndView("errors/404");
    }
}