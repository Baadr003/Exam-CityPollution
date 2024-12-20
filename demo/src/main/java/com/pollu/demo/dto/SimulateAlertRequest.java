// src/main/java/com/pollu/demo/dto/SimulateAlertRequest.java
package com.pollu.demo.dto;

import lombok.Data;

@Data
public class SimulateAlertRequest {
    private Long userId;
    private Long cityId;
    private int aqi;
}