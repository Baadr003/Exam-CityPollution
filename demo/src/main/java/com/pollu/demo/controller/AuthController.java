// AuthController.java
package com.pollu.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.pollu.demo.dto.*;
import com.pollu.demo.services.UserService;
import com.pollu.demo.entities.User;
import com.pollu.demo.entities.AlertHistory;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody UserDTO userDTO) {
        try {
            User user = userService.registerUser(userDTO);
            return ResponseEntity.ok(new AuthResponseDTO("Inscription réussie", true, user.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponseDTO("Erreur lors de l'inscription: " + e.getMessage(), false, null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO authRequest) {
        try {
            User user = userService.authenticateUser(authRequest);
            return ResponseEntity.ok(new AuthResponseDTO("Connexion réussie", true, user.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponseDTO("Erreur de connexion: " + e.getMessage(), false, null));
        }
    }

    @PutMapping("/preferences/{userId}")
    public ResponseEntity<?> updatePreferences(
            @PathVariable Long userId,
            @Valid @RequestBody UserPreferencesDTO preferencesDTO) {
        try {
            User user = userService.updatePreferences(userId, preferencesDTO);
            return ResponseEntity.ok(new AuthResponseDTO("Préférences mises à jour", true, user.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponseDTO("Erreur: " + e.getMessage(), false, null));
        }
    }

    // AuthController.java - Update getAlertHistory endpoint
    @GetMapping("/alerts/history/{userId}")
    public ResponseEntity<?> getAlertHistory(@PathVariable Long userId) {
        try {
            List<AlertHistory> history = userService.getUserAlertHistory(userId);
            log.info("Alert history for user {}: {}", userId, history);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            log.error("Error getting alert history", e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/check")
    public ResponseEntity<?> checkAuthStatus() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
public ResponseEntity<?> getUserDetails(@PathVariable Long userId) {
    try {
        UserDetailsDTO details = userService.getUserDetails(userId);
        return ResponseEntity.ok(details);
    } catch (Exception e) {
        return ResponseEntity.badRequest()
                .body(new AuthResponseDTO("Erreur: " + e.getMessage(), false, null));
    }
}
}