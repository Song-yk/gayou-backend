package com.gayou.email.controller;

import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gayou.email.service.EmailService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/join")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest request) {
        Random random = new Random();
        String verificationCode = String.format("%06d", random.nextInt(999999));
        try {
            emailService.sendVerificationEmail(request.getEmail(), "Email Verification", verificationCode);
            return ResponseEntity.ok(verificationCode);
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Failed to send email!");
        }
    }

    static class EmailRequest {
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
