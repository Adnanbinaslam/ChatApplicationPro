package com.chatApplication.chatapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.chatApplication.chatapp.service.MyAppUserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final MyAppUserService appUserService;

    private final EmailVerificationSuccessHandler emailVerificationSuccessHandler;  
    

    public SecurityConfig(MyAppUserService appUserService, EmailVerificationSuccessHandler emailVerificationSuccessHandler) {          
        this.appUserService = appUserService;
        this.emailVerificationSuccessHandler = emailVerificationSuccessHandler;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return appUserService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(httpForm -> {
                    httpForm.loginPage("/login")
                            .defaultSuccessUrl("/login-success", true)
                            .successHandler(emailVerificationSuccessHandler)
                            .failureUrl("/login?error")
                            .permitAll();   
                })
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/req/signup", "/verify-email-pending",
                        "/verify-login",  "/css/**", "/js/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                 .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        )
                .authenticationProvider(authenticationProvider())
                .build();
    }
}