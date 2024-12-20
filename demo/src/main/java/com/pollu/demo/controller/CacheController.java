package com.pollu.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class CacheController {

    @Autowired
    private CacheManager cacheManager;

    @GetMapping("/caches")
    public Collection<String> getCaches() {
        return cacheManager.getCacheNames();
    }

    @GetMapping("/caches/{cacheName}")
    public Map<Object, Object> getCacheContent(@PathVariable String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            // Assuming Ehcache is used, we need to cast to the appropriate type
            org.ehcache.Cache<Object, Object> ehcache = (org.ehcache.Cache<Object, Object>) cache.getNativeCache();
            return StreamSupport.stream(ehcache.spliterator(), false)
                .collect(Collectors.toMap(
                    entry -> entry.getKey(),
                    entry -> entry.getValue()
                ));
        }
        return Map.of("error", "Cache not found");
    }
}