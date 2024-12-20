// AlertHistory.java
package com.pollu.demo.entities;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

// AlertHistory.java
// AlertHistory.java
@Entity
@Table(name = "alert_history")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AlertHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private Integer aqi;
    
    @Column(nullable = false)
    private Double latitude;
    
    @Column(nullable = false)
    private Double longitude;
    
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();
    
    @Column(nullable = false)
    private String message;
    
    @Column(name = "city_name", nullable = false)
    private String cityName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertPriority priority;
}