package com.pollu.demo.dto;

import lombok.Data;

@Data
public class UserDetailsDTO {
    private String username;
    private String email;
    private UserPreferencesDTO preferences;
}