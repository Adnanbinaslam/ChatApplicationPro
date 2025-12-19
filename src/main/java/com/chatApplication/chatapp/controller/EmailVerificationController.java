package com.chatApplication.chatapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import com.chatApplication.chatapp.model.VerificationToken;
import com.chatApplication.chatapp.repository.TokenVerificationRepository;
import com.chatApplication.chatapp.service.MyAppUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class EmailVerificationController {

    @Autowired
    private TokenVerificationRepository tokenRepository;

    @Autowired
    private MyAppUserService userDetailsService;


    @GetMapping("/verify-login")
    public String verifyLoginToken( @RequestParam("token") String token, HttpServletRequest request) {

        Optional<VerificationToken> tokenOpt = tokenRepository.findByToken(token);
        
        if (tokenOpt.isEmpty()) {
            return "redirect:/login?error=invalid_token";
        }

        VerificationToken verificationToken = tokenOpt.get();

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(verificationToken); // Clean up expired token
            return "redirect:/login?error=token_expired";
        }


        String username = verificationToken.getUser().getUsername();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetails(request));

        // 4. Set authentication in security context
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // 5. Save to session
        HttpSession session = request.getSession(true);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                context);

        tokenRepository.delete(verificationToken);

        return "redirect:/home";


    }
}
