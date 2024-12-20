package com.pollu.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Min(value = 1, message = "Le seuil AQI doit être entre 1 et 5")
    @Max(value = 5, message = "Le seuil AQI doit être entre 1 et 5")
    @Column(name = "aqi_threshold")
    private Integer aqiThreshold = 3; // Niveau par défaut: Malsain

    @Column(name = "email_notifications_enabled")
    private Boolean emailNotificationsEnabled = true;
    
    @Column(name = "app_notifications_enabled")
    private Boolean appNotificationsEnabled = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AlertHistory> alertHistory;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}