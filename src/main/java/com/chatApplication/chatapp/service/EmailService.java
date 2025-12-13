    package com.chatApplication.chatapp.service;

    import java.nio.charset.StandardCharsets;

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

        public String sendVerificationEmail(String toEmail, String verificationLink) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setFrom("hydrousff@gmail.com");
                helper.setTo(toEmail);
                helper.setSubject("Verify Your Login");

                // Load HTML from src/main/resources/templates/email-content.html
                try (var inputStream = java.util.Objects.requireNonNull(
                        EmailService.class.getResourceAsStream("/templates/email-content.html"),
                        "HTML template not found")) {

                    String htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

                // Replace placeholders with actual values
                htmlContent = htmlContent.replace("{{EMAIL}}", toEmail);
                htmlContent = htmlContent.replace("{{VERIFICATION_LINK}}", verificationLink);
                    // true → this is HTML
                    helper.setText(htmlContent, true);
                }

                mailSender.send(message);

                return "HTML email sent!";
            } catch (Exception e) {
                return "Error while sending HTML email: " + e.getMessage();
            }
        }

    }
