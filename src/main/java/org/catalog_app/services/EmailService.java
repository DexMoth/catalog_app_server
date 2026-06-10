package org.catalog_app.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Подтверждение регистрации");
        message.setText("Ваш код подтверждения: " + formatCode(code) + "\n\n"
                + "Код действителен в течение 10 минут.\n"
                + "Никому не сообщайте этот код.");

        mailSender.send(message);
    }

    private String formatCode(String code) {
        if (code == null || code.length() != 6) {
            return code;
        }
        return code.substring(0, 3) + " " + code.substring(3, 6);
    }
}