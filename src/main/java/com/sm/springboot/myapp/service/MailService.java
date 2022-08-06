package com.sm.springboot.myapp.service;

import com.sm.springboot.myapp.exception.SpringAppException;
import com.sm.springboot.myapp.dto.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
class MailService {

    private final JavaMailSender mailSender;

    @Async
    void sendMail(NotificationEmail notificationEmail) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(notificationEmail.getRecipient());
        mailMessage.setSubject(notificationEmail.getSubject());
        mailMessage.setText(notificationEmail.getBody());

        mailMessage.setFrom("louis@example.com");

        try {
            mailSender.send(mailMessage);
            log.info("Activation email sent.");
        } catch (MailException e) {
            log.error("Exception occurred when sending mail", e);
            throw new SpringAppException("Exception occurred when sending mail to " + notificationEmail.getRecipient());
        }
    }

}
