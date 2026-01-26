package com.farmlink.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendResetMail(String toEmail, String token) {

        String resetLink =
            "http://localhost:5173/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Reset Password - FarmLink");
        message.setText(
            "Click the link to reset your password:\n\n" +
            resetLink +
            "\n\nThis link is valid for 15 minutes."
        );

        mailSender.send(message);
    }
}
