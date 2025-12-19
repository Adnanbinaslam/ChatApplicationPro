package com.chatApplication.chatapp.security;

import com.chatApplication.chatapp.model.MyAppUser;
import com.chatApplication.chatapp.repository.MyAppUserRepository;
import com.chatApplication.chatapp.service.EmailService;
import com.chatApplication.chatapp.service.TokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class EmailVerificationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private EmailService emailService;

      @Autowired
    private TokenService tokenService;

    @Autowired
    private MyAppUserRepository userRepository;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        // 1
        String username = authentication.getName();

        // 2
        MyAppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));


        //create verification token
        String token = tokenService.createAndSaveVerificationToken(user);

        //check if user authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            response.sendRedirect("/login");
            return;
        }


        //send verification email
        String email = user.getEmail();
        String verificationLink = baseUrl + "/verify-login?token=" + token;

        emailService.sendVerificationEmail(email, verificationLink);

        // Step 4: IMPORTANT — log user out (but keep session)
        SecurityContextHolder.clearContext();

        response.sendRedirect("/verify-email-pending?email=" + email);
    }
}
