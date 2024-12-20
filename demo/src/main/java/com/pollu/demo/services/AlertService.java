package com.pollu.demo.services;

import com.pollu.demo.dto.AlertMessage;
import com.pollu.demo.entities.AlertHistory;
import com.pollu.demo.entities.AlertPriority;
import com.pollu.demo.entities.FavoriteCity;
import com.pollu.demo.entities.User;
import com.pollu.demo.repositories.AlertHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AlertService {
    @Autowired
    private AlertHistoryRepository alertHistoryRepository;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private EmailService emailService;

    public void processAlert(User user, FavoriteCity city, int newAqi) {
        try {
            if (!shouldProcessAlert(user, city, newAqi)) {
                log.debug("Alert skipped for user {} and city {}", user.getUsername(), city.getCityName());
                return;
            }

            AlertPriority priority = AlertPriority.fromAQI(newAqi);
            AlertHistory alert = createAlert(user, city, newAqi, priority);
            
            // App notifications
            if (user.getAppNotificationsEnabled()) {
                try {
                    sendAppNotification(user, alert);
                } catch (Exception e) {
                    log.error("Failed to send app notification to user {}: {}", user.getUsername(), e.getMessage());
                }
            }

            // Email notifications
            if (user.getEmailNotificationsEnabled() && isValidEmail(user.getEmail())) {
                try {
                    sendEmailNotification(user, alert);
                } catch (Exception e) {
                    log.error("Failed to send email to {}: {}", user.getEmail(), e.getMessage());
                }
            }

            log.info("Alert processed successfully for city {} and user {}", city.getCityName(), user.getUsername());
        } catch (Exception e) {
            log.error("Error processing alert for city {}: {}", city.getCityName(), e.getMessage());
            throw new RuntimeException("Erreur lors du traitement de l'alerte", e);
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private AlertHistory createAlert(User user, FavoriteCity city, int aqi, AlertPriority priority) {
        AlertHistory alert = new AlertHistory();
        alert.setUser(user);
        alert.setAqi(aqi);
        alert.setPriority(priority);
        alert.setLatitude(city.getLatitude());
        alert.setLongitude(city.getLongitude());
        alert.setCityName(city.getCityName());
        alert.setTimestamp(LocalDateTime.now());
        alert.setMessage(String.format("Alerte Niveau %d (%s) : AQI de %s est à %d", 
            priority.getLevel(), 
            priority.getLabel(), 
            city.getCityName(), 
            aqi));
        
        return alertHistoryRepository.save(alert);
    }

    private void sendAppNotification(User user, AlertHistory alert) {
        AlertMessage message = new AlertMessage(alert);
        messagingTemplate.convertAndSendToUser(
            user.getUsername(),
            "/topic/alerts",
            message
        );
        log.info("App notification sent to user: {}", user.getUsername());
    }

    private void sendEmailNotification(User user, AlertHistory alert) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("cityName", alert.getCityName());
        templateModel.put("aqi", alert.getAqi());
        templateModel.put("priority", alert.getPriority().getLabel());
        templateModel.put("priorityColor", alert.getPriority().getColor());
        templateModel.put("message", alert.getMessage());
        templateModel.put("timestamp", 
            alert.getTimestamp().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        String subject = String.format("Alerte Qualité de l'Air - %s", alert.getCityName());
        
        emailService.sendAlertEmail(user.getEmail(), subject, templateModel);
        log.info("Email alert sent to: {}", user.getEmail());
    }

    @Cacheable(value = "userAlerts", key = "#user.id + '_' + #city.id")
    public boolean shouldProcessAlert(User user, FavoriteCity city, int newAqi) {
        if (!user.getAppNotificationsEnabled() && !user.getEmailNotificationsEnabled()) {
            return false;
        }
        
        AlertPriority currentPriority = AlertPriority.fromAQI(newAqi);
        AlertPriority userThreshold = AlertPriority.fromLevel(user.getAqiThreshold());
        return shouldSendAlert(currentPriority, userThreshold);
    }

    private boolean shouldSendAlert(AlertPriority currentPriority, AlertPriority userThreshold) {
        return currentPriority.getLevel() >= userThreshold.getLevel();
    }
}