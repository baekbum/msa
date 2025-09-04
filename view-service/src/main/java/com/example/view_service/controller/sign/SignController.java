package com.example.view_service.controller.sign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("sign")
public class SignController {

    @GetMapping("login")
    public String login() {
        return "sign/login";
    }

    @GetMapping("register")
    public String register() {
        return "sign/register";
    }

    @GetMapping("forgot-password")
    public String forgotPassword() {
        return "sign/forgot-password";
    }
}