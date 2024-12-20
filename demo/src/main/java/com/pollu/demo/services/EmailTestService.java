package com.pollu.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class EmailTestService {
    @Autowired
    private EmailService emailService;

    public void sendTestEmail(String email) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("cityName", "Paris");
        templateModel.put("aqi", 180);
        templateModel.put("priority", "Malsain");
        templateModel.put("priorityColor", "#ff7e00");
        templateModel.put("message", "Test du nouveau template d'alerte - La qualité de l'air nécessite votre attention");
        templateModel.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        emailService.sendAlertEmail(
            email,
            "PolluAlert - Test Nouveau Template",
            templateModel
        );
        
        log.info("Test email with new template sent to: {}", email);
    }
}