package com.pollu.demo.repositories;

import com.pollu.demo.entities.FavoriteCity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FavoriteCityRepository extends JpaRepository<FavoriteCity, Long> {
    List<FavoriteCity> findByUserId(Long userId);
    boolean existsByUserIdAndLatitudeAndLongitude(Long userId, Double latitude, Double longitude);
    Optional<FavoriteCity> findByIdAndUserId(Long id, Long userId);
}