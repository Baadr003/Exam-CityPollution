// src/main/java/com/pollu/demo/entities/FavoriteCity.java
package com.pollu.demo.entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "favorite_cities")
@Data
public class FavoriteCity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String cityName;
    private Double latitude;
    private Double longitude;
    private Integer currentAqi;
    private LocalDateTime lastChecked;
}