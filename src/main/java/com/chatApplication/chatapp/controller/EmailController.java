// package com.chatApplication.chatapp.controller;

// import java.io.File;
// import java.nio.charset.StandardCharsets;

// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.mail.javamail.MimeMessageHelper;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import jakarta.mail.internet.MimeMessage;

// @RestController
// public class EmailController {

//     private final JavaMailSender mailSender;

//     public EmailController(JavaMailSender mailSender) {
//         this.mailSender = mailSender;
//     }

//     // @RequestMapping("/send-email")
//     // public String sendEmail() {
//     //     try {
//     //         SimpleMailMessage message = new SimpleMailMessage();
//     //         message.setFrom("hydrousff@gmail.com");
//     //         message.setTo("hydrousff@gmail.com");
//     //         message.setSubject("simple test email from chat application");
//     //         message.setText("This is a simple test email sent from the chat application.");
//     //         mailSender.send(message);
//     //         return "Email sent!";
//     //     } catch (Exception e) {
//     //         return "Error while sending email: " + e.getMessage();
//     //     }
//     // }

//     // @RequestMapping("/send-email-with-attachment")
//     //     public String sendEmailWithAttachment(){
//     //         try{
//     //         MimeMessage message = mailSender.createMimeMessage();
//     //         MimeMessageHelper helper = new MimeMessageHelper(message,true);

//     //         helper.setFrom("hydrousff@gmail.com");
//     //         helper.setTo("hydrousff@gmail.com");
//     //         helper.setSubject("Java email with attachment from chat application");
//     //         helper.setText("please find the attachment below.");
//     //         helper.addAttachment("photo.png", new File("C:\\Users\\user\\Downloads\\Stripe_Cover_Letter_Adnan.docx"));
//     //         mailSender.send(message);

//     //         return "Email with attachment sent!";
//     //         }catch(Exception e){
//     //             return "Error while sending email with attachment: " + e.getMessage();
//     //         }
//     //     }

//     @RequestMapping("/send-html-email")
//     public String sendHtmlEmail() {
//         try {
//             MimeMessage message = mailSender.createMimeMessage();
//             MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

//             helper.setFrom("hydrousff@gmail.com");
//             helper.setTo("hydrousff@gmail.com");
//             helper.setSubject("HTML Email from Chat Application");

//             // Load HTML from src/main/resources/templates/email-content.html
//             try (var inputStream = java.util.Objects.requireNonNull(
//                     EmailController.class.getResourceAsStream("/templates/email-content.html"),
//                     "HTML template not found")) {

//                 String htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

//                 // true → this is HTML
//                 helper.setText(htmlContent, true);
//             }

//             mailSender.send(message);

//             return "HTML email sent!";
//         } catch (Exception e) {
//             return "Error while sending HTML email: " + e.getMessage();
//         }
//     }

// }

package com.chatApplication.chatapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class EmailController {

    // @Autowired
    // private TokenVerificationRepository tokenRepository;

    // @Autowired
    // private MyAppUserService userDetailsService;

    @GetMapping("/verify-email-pending")
    public String pendingEmailPage(String email) {
        return "A verification link has been sent to: " + email;
    }

    @GetMapping("/verify-login")
    public String verifyLoginToken(String token) {
    return "Token received: " + token + " (You will handle verification logichere)";
    }

    // @GetMapping("/verify-login")
    // public String verifyLoginToken(@RequestParam("token") String token) {

    //     Optional<VerificationToken> tokenOpt = tokenRepository.findByToken(token);
    //     VerificationToken verificationToken = tokenOpt.get();

    //     // Validation logic...
    //     if (tokenOpt.isEmpty()) {
    //         return "redirect:/login?error=invalid_token";
    //     }

    //     if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
    //         return "redirect:/login?error=token_expired";
    //     }

    //     // Get the email from the token
    //     String email = verificationToken.getUser().getEmail();

    //     // Load UserDetails from the service
    //     UserDetails userDetails = userDetailsService.loadUserByUsername(email);

    //     // Create authentication token with UserDetails
    //     UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
    //             userDetails, // ✅ UserDetails object
    //             null, // ✅ No credentials needed
    //             userDetails.getAuthorities() // ✅ UserDetails has getAuthorities()
    //     );

    //     SecurityContextHolder.getContext().setAuthentication(authentication);

    //     // Redirect to success page
    //     return "redirect:/login-success";
    // }

    // OPTIONAL: For manual testing
    @GetMapping("/test-email")
    public String testEmail() {
        return "Use this endpoint to manually test email sending.";
    }
}
