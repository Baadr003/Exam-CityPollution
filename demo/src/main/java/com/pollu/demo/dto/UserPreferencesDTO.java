// src/main/java/com/pollu/demo/dto/UserPreferencesDTO.java
package com.pollu.demo.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPreferencesDTO {
    private Integer aqiThreshold;
    private Boolean emailNotificationsEnabled;
    private Boolean appNotificationsEnabled;
}