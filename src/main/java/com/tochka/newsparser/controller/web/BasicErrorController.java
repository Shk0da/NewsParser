package com.tochka.newsparser.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class BasicErrorController implements ErrorController {

    public static final String ERROR_PATH = "/error";

    @Autowired
    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @RequestMapping(value = ERROR_PATH)
    public ModelAndView error(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(
                "errors/" + String.valueOf(response.getStatus()),
                getErrorAttributes(request),
                HttpStatus.valueOf(response.getStatus())
        );
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request) {
        return errorAttributes.getErrorAttributes(new ServletRequestAttributes(request), false);
    }
}
