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

    /**
     * 이메일 인증 코드를 전송하는 메서드
     *
     * @param request - 이메일 요청 객체 (EmailRequest)
     * @return ResponseEntity<?> - 전송된 인증 코드 또는 오류 메시지
     */
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

    // 내부 클래스 EmailRequest: 이메일 요청 데이터 전달
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
