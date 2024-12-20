package com.pollu.demo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;

    private static final String FROM_EMAIL = "citypollutioncitypollution@gmail.com";

    public void sendAlertEmail(String to, String subject, Map<String, Object> templateModel) {
        sendTemplatedEmail(to, subject, "alert-email", templateModel);
    }

    public void sendVerificationEmail(String to, String subject, Map<String, Object> templateModel) {
        sendTemplatedEmail(to, subject, "verification-email", templateModel);
    }

    public void sendPasswordResetEmail(String to, String subject, Map<String, Object> templateModel) {
        sendTemplatedEmail(to, subject, "reset-password-email", templateModel);
    }

    private void sendTemplatedEmail(String to, String subject, String template, Map<String, Object> templateModel) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            Context context = new Context();
            context.setVariables(templateModel);
            
            String htmlContent = templateEngine.process(template, context);
            
            helper.setFrom(FROM_EMAIL);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            emailSender.send(message);
            log.info("{} email sent to: {}", template, to);
            
        } catch (MessagingException e) {
            log.error("Failed to send {} email to {}: {}", template, to, e.getMessage());
            throw new RuntimeException("Error sending email", e);
        }
    }
}