
package com.chatApplication.chatapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/req/signup")
    public String signup() {
        return "signup";
    }
    
    @GetMapping("/home")
    public String home() {
        return "home";
    }
    
    @GetMapping("/login-success")
    public String loginSuccess() {
        return "login-success";
    }
    
    @GetMapping("/verify-email-pending")
    public String pendingEmailPage() {
        return "verify-email-pending";
    }
}