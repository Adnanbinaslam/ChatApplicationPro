package com.chatApplication.chatapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SucessLogin {

    @GetMapping("/login-success")
    public String loginSuccess() {
        return "login-success";
    }
    
}
