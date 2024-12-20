package com.pollu.demo.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.pollu.demo.dto.AlertMessage;
import com.pollu.demo.dto.BaseResponseDTO;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Service
@Slf4j
public class PollutionService {
    private final WebClient webClient;
    private final String apiKey;
    private final SimpMessagingTemplate messagingTemplate;
    private final EmailService emailService;

    @Value("${openweathermap.api.key}")
    private String configuredApiKey;

    @PostConstruct
    public void init() {
        log.info("API Key configured: {}", configuredApiKey != null && !configuredApiKey.isEmpty());
        if (configuredApiKey == null || configuredApiKey.isEmpty()) {
            throw new IllegalStateException("OpenWeatherMap API key not configured");
        }
    }

    public PollutionService(
            WebClient.Builder webClientBuilder,
            @Value("${openweathermap.api.key}") String apiKey,
            SimpMessagingTemplate messagingTemplate,
            EmailService emailService) {
        this.apiKey = apiKey;
        this.messagingTemplate = messagingTemplate;
        this.emailService = emailService;
        
        log.debug("Initializing WebClient with API key: {}", apiKey.substring(0, 5) + "...");
        
        this.webClient = webClientBuilder
            .baseUrl("https://api.openweathermap.org/data/2.5/air_pollution")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .filter((request, next) -> {
                log.debug("Making request to: {}", request.url());
                return next.exchange(request);
            })
            .build();
    }
    
    @Cacheable("pollutionForecast")
    public BaseResponseDTO getForecast(double lat, double lon) {
        log.debug("Fetching forecast for lat: {}, lon: {}", lat, lon);
        String url = String.format("/forecast?lat=%s&lon=%s&appid=%s", lat, lon, apiKey.trim());
        
        return webClient.get()
            .uri(url)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response -> {
                if (response.statusCode() == HttpStatus.UNAUTHORIZED) {
                    log.error("API key authentication failed for forecast");
                    return Mono.error(new RuntimeException("Invalid API key"));
                }
                return Mono.error(new RuntimeException("API error: " + response.statusCode()));
            })
            .bodyToMono(BaseResponseDTO.class)
            .doOnError(error -> log.error("Forecast API call failed: {}", error.getMessage()))
            .doOnSuccess(data -> log.info("Successfully retrieved forecast data"))
            .block();
    }

    @Cacheable("pollutionHistory")
    public BaseResponseDTO getHistory(double lat, double lon, long start, long end) {
        log.debug("Fetching history for lat: {}, lon: {}, start: {}, end: {}", lat, lon, start, end);
        String url = String.format("/history?lat=%s&lon=%s&start=%s&end=%s&appid=%s", 
            lat, lon, start, end, apiKey.trim());
        
        return webClient.get()
            .uri(url)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response -> {
                if (response.statusCode() == HttpStatus.UNAUTHORIZED) {
                    log.error("API key authentication failed for history");
                    return Mono.error(new RuntimeException("Invalid API key"));
                }
                return Mono.error(new RuntimeException("API error: " + response.statusCode()));
            })
            .bodyToMono(BaseResponseDTO.class)
            .doOnError(error -> log.error("History API call failed: {}", error.getMessage()))
            .doOnSuccess(data -> log.info("Successfully retrieved historical data"))
            .block();
    }
    
    @Cacheable("currentPollution")
    public BaseResponseDTO getCurrentPollution(double lat, double lon) {
        String url = String.format("?lat=%s&lon=%s&appid=%s", lat, lon, apiKey.trim());
        log.debug("Calling API with URL: {}", url);
        
        return webClient.get()
            .uri(url)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response -> {
                if (response.statusCode() == HttpStatus.UNAUTHORIZED) {
                    log.error("API key authentication failed");
                    return Mono.error(new RuntimeException("Invalid API key"));
                }
                return Mono.error(new RuntimeException("API error: " + response.statusCode()));
            })
            .bodyToMono(BaseResponseDTO.class)
            .doOnError(error -> log.error("API call failed: {}", error.getMessage()))
            .doOnSuccess(data -> log.info("Successfully retrieved pollution data"))
            .block();
    }
}


