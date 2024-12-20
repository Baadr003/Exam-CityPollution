package com.pollu.demo;

// src/test/java/com/pollu/demo/services/UserServiceTest.java

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.pollu.demo.repositories.UserRepository;
import com.pollu.demo.services.UserService;
import com.pollu.demo.entities.User;
import com.pollu.demo.dto.UserDetailsDTO;
import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setEmail("test@example.com");
        testUser.setAqiThreshold(3);
        testUser.setEmailNotificationsEnabled(true);
        testUser.setAppNotificationsEnabled(true);
    }

    @Test
    void getUserDetails_ShouldReturnCorrectDTO() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        UserDetailsDTO details = userService.getUserDetails(1L);

        // Then
        assertNotNull(details);
        assertEquals("testUser", details.getUsername());
        assertEquals("test@example.com", details.getEmail());
        assertEquals(3, details.getPreferences().getAqiThreshold());
        assertTrue(details.getPreferences().getEmailNotificationsEnabled());
        assertTrue(details.getPreferences().getAppNotificationsEnabled());
    }

    @Test
    void getUserById_ShouldReturnUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        User foundUser = userService.getUserById(1L);

        // Then
        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
        assertEquals("testUser", foundUser.getUsername());
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserNotFound() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Then
        assertThrows(RuntimeException.class, () -> {
            userService.getUserById(999L);
        });
    }
}