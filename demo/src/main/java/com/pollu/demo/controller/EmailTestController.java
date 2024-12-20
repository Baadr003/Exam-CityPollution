package com.pollu.demo.controller;

import com.pollu.demo.services.EmailTestService;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:3000")
public class EmailTestController {
    @Autowired
    private EmailTestService emailTestService;

    @PostMapping("/email")
    public ResponseEntity<?> testEmail(@RequestParam String email) {
        try {
            emailTestService.sendTestEmail(email);
            return ResponseEntity.ok()
                .body(Map.of("message", "Test email sent successfully to " + email));
        } catch (Exception e) {
            log.error("Failed to send test email: ", e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to send test email: " + e.getMessage()));
        }
    }
}
