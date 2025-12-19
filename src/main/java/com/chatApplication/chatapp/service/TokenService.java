package com.chatApplication.chatapp.service;

import com.chatApplication.chatapp.model.MyAppUser;
import com.chatApplication.chatapp.model.VerificationToken;
import com.chatApplication.chatapp.repository.TokenVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {
    
    @Autowired
    private TokenVerificationRepository tokenRepository;
    
    @Value("${app.token.expiry-minutes:10}")
    private int tokenExpiryMinutes;
    
    /**
     * Create and save a verification token for a user
     */
    public String createAndSaveVerificationToken(MyAppUser user) {
        String token = UUID.randomUUID().toString();
        
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(tokenExpiryMinutes))
                .build();
        
        tokenRepository.save(verificationToken);
        return token;
    }
    
    /**
     * Get verification token by token string
     */
    public Optional<VerificationToken> getVerificationToken(String token) {
        return tokenRepository.findByToken(token);
    }
    
    /**
     * Check if token is expired
     */
    public boolean isTokenExpired(VerificationToken token) {
        return token.getExpiryDate().isBefore(LocalDateTime.now());
    }
    
    /**
     * Delete a verification token
     */
    public void deleteToken(VerificationToken token) {
        tokenRepository.delete(token);
    }
}