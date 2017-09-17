package com.tochka.newsparser.controller.web;

import com.tochka.newsparser.domain.User;
import com.tochka.newsparser.repository.UserRepository;
import com.tochka.newsparser.service.SecurityService;
import com.tochka.newsparser.utils.ConstraintViolationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;

@Controller
public class AuthController {

    public static final String LOGIN_PATH = "auth/login";
    public static final String REGISTER_PATH = "auth/register";

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/login")
    public String login(Model model, String error) {
        model.addAttribute("error", error);
        return LOGIN_PATH;
    }

    @GetMapping(value = "/logout")
    public String logout(Model model) {
        model.addAttribute("logout", true);
        return LOGIN_PATH;
    }

    @GetMapping(value = "/register")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        return REGISTER_PATH;
    }

    @PostMapping(value = "/register")
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors() || userForm.getLogin() == null || userForm.getEmail() == null
                || userRepository.findOneByLogin(userForm.getLogin().toLowerCase()).isPresent()
                || userRepository.findOneByEmail(userForm.getEmail()).isPresent()) {
            model.addAttribute("error", "Login or Email exists");
            return REGISTER_PATH;
        }

        try {
            securityService.createUser(userForm);
        } catch (ConstraintViolationException ex) {
            model.addAttribute("error", ConstraintViolationUtils.getMessageViolationException(ex));
            return REGISTER_PATH;
        }

        return "redirect:/";
    }
}
