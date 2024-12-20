package com.pollu.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FavoriteCityDTO {
    private Long id;
    private String cityName;
    private Double latitude;
    private Double longitude;
    private Integer currentAqi;
    private LocalDateTime lastChecked;
}