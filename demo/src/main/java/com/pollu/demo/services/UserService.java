// UserService.java
package com.pollu.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pollu.demo.dto.*;
import com.pollu.demo.entities.*;
import com.pollu.demo.repositories.*;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AlertHistoryRepository alertHistoryRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(), 
            user.getPassword(), 
            Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }

    public User registerUser(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Ce nom d'utilisateur existe déjà");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(Role.USER);

        if (userDTO.getPreferences() != null) {
            user.setAqiThreshold(userDTO.getPreferences().getAqiThreshold());
            user.setEmailNotificationsEnabled(userDTO.getPreferences().getEmailNotificationsEnabled());
            user.setAppNotificationsEnabled(userDTO.getPreferences().getAppNotificationsEnabled());
        }

        return userRepository.save(user);
    }

    public User authenticateUser(AuthRequestDTO authRequest) {
        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }
        
        return user;
    }

    public User getUserFromAuthentication(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    public User updatePreferences(Long userId, UserPreferencesDTO preferencesDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        user.setAqiThreshold(preferencesDTO.getAqiThreshold());
        user.setEmailNotificationsEnabled(preferencesDTO.getEmailNotificationsEnabled());
        user.setAppNotificationsEnabled(preferencesDTO.getAppNotificationsEnabled());

        return userRepository.save(user);
    }
    public UserDetailsDTO getUserDetails(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        UserDetailsDTO details = new UserDetailsDTO();
        details.setUsername(user.getUsername());
        details.setEmail(user.getEmail());
        
        UserPreferencesDTO prefs = new UserPreferencesDTO();
        prefs.setAqiThreshold(user.getAqiThreshold());
        prefs.setEmailNotificationsEnabled(user.getEmailNotificationsEnabled());
        prefs.setAppNotificationsEnabled(user.getAppNotificationsEnabled());
        details.setPreferences(prefs);
        
        return details;
    }
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    public List<AlertHistory> getUserAlertHistory(Long userId) {
        List<AlertHistory> alerts = alertHistoryRepository.findByUserIdOrderByTimestampDesc(userId);
        log.info("Found {} alerts for user {}", alerts.size(), userId);
        alerts.forEach(alert -> log.debug("Alert: {}", alert));
        return alerts;
    }
    
}