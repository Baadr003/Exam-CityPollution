// DemoApplication.java
package com.pollu.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class DemoApplication {
    
    // Constantes pour l'API OpenWeather
    public static final String OPENWEATHER_API_BASE_URL = "https://api.openweathermap.org/data/2.5/air_pollution";
    
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}