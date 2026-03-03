package com.motchecker.mot_reminder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        try {
            logger.debug("Attempting to send email to {}", to);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("mot.reminder.pacalt@seznam.cz");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            emailSender.send(message);

            logger.info("Email successfully sent to: {}", to);

        } catch (Exception e) {
            logger.error("Failed to send email to {}. Error: {}", to, e.getMessage(), e);
        }
    }
}