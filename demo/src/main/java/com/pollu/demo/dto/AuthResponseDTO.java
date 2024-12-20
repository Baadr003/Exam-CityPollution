package com.pollu.demo.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String message;
    private boolean success;
    private Long userId;
}