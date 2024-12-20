// AlertMessage.java
package com.pollu.demo.dto;

import com.pollu.demo.entities.AlertHistory;
import com.pollu.demo.entities.AlertPriority;
import lombok.Data;
import java.time.Instant;
import java.time.ZoneOffset;

// AlertMessage.java
@Data
public class AlertMessage {
    private final double latitude;
    private final double longitude;
    private final int aqi;
    private final String cityName;
    private final AlertPriority priority;
    private final String message;
    private final Instant timestamp;

    public AlertMessage(double latitude, double longitude, int aqi, 
                       String cityName, AlertPriority priority) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.aqi = aqi;
        this.cityName = cityName;
        this.priority = priority;
        this.timestamp = Instant.now();
        this.message = formatMessage();
    }

    public AlertMessage(AlertHistory alert) {
        if (alert == null) {
            throw new IllegalArgumentException("Alert cannot be null");
        }
        this.latitude = alert.getLatitude();
        this.longitude = alert.getLongitude();
        this.aqi = alert.getAqi();
        this.cityName = alert.getCityName();
        this.priority = alert.getPriority();
        this.timestamp = alert.getTimestamp().toInstant(ZoneOffset.UTC);
        this.message = alert.getMessage() != null ? alert.getMessage() : formatMessage();
    }

    private String formatMessage() {
        return String.format("Alerte %s : AQI de %s est à %d (Niveau: %s)", 
            priority.getLabel(), 
            cityName,
            aqi,
            priority.getLabel());
    }
}