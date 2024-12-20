package com.pollu.demo.controller;

import com.pollu.demo.entities.AlertHistory;
import com.pollu.demo.entities.User;
import com.pollu.demo.repositories.UserRepository;
import com.pollu.demo.repositories.AlertHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin(origins = "http://localhost:3000")
public class AlertController {

    @Autowired
    private AlertHistoryRepository alertHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/history/{userId}")
    public ResponseEntity<?> getAlertHistory(@PathVariable Long userId) {
        try {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            
            if (!user.getAppNotificationsEnabled()) {
                return ResponseEntity.ok(Collections.emptyList());
            }
            
            List<AlertHistory> alerts = alertHistoryRepository.findByUserIdOrderByTimestampDesc(userId);
            return ResponseEntity.ok(alerts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}