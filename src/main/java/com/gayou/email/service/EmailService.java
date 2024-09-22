package com.gayou.email.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * 이메일 인증 코드를 전송하는 메서드
     *
     * @param to               - 수신자 이메일 주소
     * @param subject          - 이메일 제목
     * @param verificationCode - 인증 코드
     * @throws MessagingException - 이메일 전송 시 발생할 수 있는 예외 처리
     */
    public void sendVerificationEmail(String to, String subject, String verificationCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText("Your verification code is: " + verificationCode, true);
        mailSender.send(message);
    }
}
