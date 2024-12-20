package com.pollu.demo.config;

import org.springframework.cache.interceptor.KeyGenerator;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public KeyGenerator favoriteCityKeyGenerator() {
        return (target, method, params) -> {
            StringBuilder key = new StringBuilder();
            key.append(params[0]); // latitude
            key.append("_");
            key.append(params[1]); // longitude
            return key.toString();
        };
    }
}