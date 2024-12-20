// UserDTO.java
package com.pollu.demo.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class UserDTO {
    @NotBlank(message = "Le nom d'utilisateur est requis")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "L'email est requis")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est requis")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;
    
    private UserPreferencesDTO preferences;
}