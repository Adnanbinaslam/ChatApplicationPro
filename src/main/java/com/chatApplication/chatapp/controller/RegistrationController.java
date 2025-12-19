package com.chatApplication.chatapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.chatApplication.chatapp.model.MyAppUser;
import com.chatApplication.chatapp.repository.MyAppUserRepository;

@Controller
public class RegistrationController {

    private final MyAppUserRepository repository;

    private final PasswordEncoder passwordEncoder;

    public RegistrationController(MyAppUserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/req/signup", consumes = "application/json")
    public ResponseEntity<?> createUser(@RequestBody MyAppUser user) {
        
        // Check if username already exists
        if (repository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
     
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
        return ResponseEntity.ok().body("User registered successfully");
    }

}
