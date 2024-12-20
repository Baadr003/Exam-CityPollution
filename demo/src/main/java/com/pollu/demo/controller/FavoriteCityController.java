package com.pollu.demo.controller;

import com.pollu.demo.dto.FavoriteCityDTO;
import com.pollu.demo.entities.FavoriteCity;
import com.pollu.demo.services.FavoriteCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:3000")
public class FavoriteCityController {
    
    @Autowired
    private FavoriteCityService favoriteCityService;

    @PostMapping
    public ResponseEntity<?> addFavoriteCity(@RequestBody FavoriteCityDTO cityDTO, @RequestParam Long userId) {
        try {
            FavoriteCity city = favoriteCityService.addFavoriteCity(userId, cityDTO);
            return ResponseEntity.ok(city);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getFavoriteCities(@RequestParam Long userId) {
        try {
            List<FavoriteCityDTO> cities = favoriteCityService.getFavoriteCities(userId);
            return ResponseEntity.ok(cities);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{cityId}")
    public ResponseEntity<?> removeFavoriteCity(@PathVariable Long cityId, @RequestParam Long userId) {
        try {
            favoriteCityService.removeFavoriteCity(userId, cityId);
            return ResponseEntity.ok().body(Map.of("message", "Ville supprimée des favoris"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}