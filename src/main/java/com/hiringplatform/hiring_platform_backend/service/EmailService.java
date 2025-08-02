package com.hiringplatform.hiring_platform_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // <-- IMPORT THIS
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * A dedicated service for handling all email-sending operations.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Reads the 'spring.mail.username' property from application.properties.
     * This will be used as the "from" address for all outgoing emails.
     */
    @Value("${spring.mail.username}")
    private String fromEmailAddress;

    /**
     * Composes and sends a simple text email.
     *
     * @param to The recipient's email address.
     * @param subject The subject line of the email.
     * @param body The main text content of the email.
     */
    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmailAddress); // <-- EXPLICITLY SET THE "FROM" ADDRESS
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            System.out.println("Email sent successfully to " + to);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }
}
