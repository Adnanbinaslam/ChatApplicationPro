package com.chatApplication.chatapp.security;

import com.chatApplication.chatapp.model.MyAppUser;
import com.chatApplication.chatapp.model.VerificationToken;
import com.chatApplication.chatapp.repository.MyAppUserRepository;
import com.chatApplication.chatapp.repository.TokenVerificationRepository;
import com.chatApplication.chatapp.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class EmailVerificationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private TokenVerificationRepository tokenRepository;

    @Autowired
    private MyAppUserRepository userRepository;

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

        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(10))
                .build();

        tokenRepository.save(verificationToken);
        if (authentication == null || !authentication.isAuthenticated()) {
            response.sendRedirect("/login");
            return;
        }

        // String email = authentication.getName();

        String email = user.getEmail();

        // Generate token

        String verificationLink = "http://localhost:8080/verify-login?token=" + token;

        emailService.sendVerificationEmail(email, verificationLink);

        // Step 4: IMPORTANT — log user out (but keep session)
        SecurityContextHolder.clearContext();

        response.sendRedirect("/verify-email-pending?email=" + email);
    }
}
