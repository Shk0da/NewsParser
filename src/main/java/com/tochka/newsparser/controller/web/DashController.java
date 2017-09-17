package com.tochka.newsparser.controller.web;

import com.tochka.newsparser.security.AuthoritiesConstants;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
public class DashController {

    @GetMapping("/")
    @Transactional(readOnly = true)
    public String main() {
        return "index";
    }
}
