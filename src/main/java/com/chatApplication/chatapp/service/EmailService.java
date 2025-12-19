package com.chatApplication.chatapp.service;

import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;


@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.email.template-path:/templates/email-content.html}")
    private String emailTemplatePath;

    public String sendVerificationEmail(String toEmail, String verificationLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Verify Your Login");

            String htmlContent = loadEmailTemplate();

            htmlContent = htmlContent.replace("{{EMAIL}}", toEmail);
            htmlContent = htmlContent.replace("{{VERIFICATION_LINK}}", verificationLink);

            helper.setText(htmlContent, true); // true → this is HTML
            mailSender.send(message);
            return "Verification email sent!";
        } catch (Exception e) {
            return "Error while sending verification email: " + e.getMessage();
        }
    }

    private String loadEmailTemplate() throws Exception {
        // Load HTML from src/main/resources/templates/email-content.html
        try (var inputStream = java.util.Objects.requireNonNull(
                EmailService.class.getResourceAsStream(emailTemplatePath),
                "HTML template not found")) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
