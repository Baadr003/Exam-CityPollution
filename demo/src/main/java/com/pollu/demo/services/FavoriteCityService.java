package com.pollu.demo.services;

import com.pollu.demo.dto.FavoriteCityDTO;
import com.pollu.demo.entities.FavoriteCity;
import com.pollu.demo.entities.User;
import com.pollu.demo.repositories.FavoriteCityRepository;
import com.pollu.demo.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FavoriteCityService {

    @Autowired
    private FavoriteCityRepository favoriteCityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PollutionService pollutionService;

    @Autowired
    private AlertService alertService;

    @Scheduled(fixedRate = 3600000) // 1 heure
    public void checkFavoriteCitiesAqi() {
        log.info("Starting scheduled check of favorite cities AQI at {}", LocalDateTime.now());
        
        List<FavoriteCity> cities = favoriteCityRepository.findAll();
        log.info("Found {} cities to check", cities.size());
        
        cities.forEach(city -> {
            try {
                log.debug("Checking city: {} for user: {}", city.getCityName(), city.getUser().getUsername());
                
                var pollution = pollutionService.getCurrentPollution(
                    city.getLatitude(), 
                    city.getLongitude()
                );

                int newAqi = pollution.getList().get(0).getMain().getAqi();
                User user = city.getUser();
                int threshold = getUserAqiThreshold(user.getId());

                log.debug("City: {}, Current AQI: {}, New AQI: {}, Threshold: {}", 
                    city.getCityName(), city.getCurrentAqi(), newAqi, threshold);

                // Toujours vérifier si l'AQI dépasse le seuil, même s'il n'a pas changé
                if (newAqi >= threshold) {
                    log.info("AQI {} exceeds threshold {} for city {}", 
                        newAqi, threshold, city.getCityName());
                    alertService.processAlert(user, city, newAqi);
                }

                // Mettre à jour l'AQI dans tous les cas
                city.setCurrentAqi(newAqi);
                city.setLastChecked(LocalDateTime.now());
                favoriteCityRepository.save(city);

                log.info("Updated AQI for city {}: previous={}, new={}", 
                    city.getCityName(), 
                    city.getCurrentAqi(), 
                    newAqi);
                } catch (Exception e) {
                    log.error("Error checking city {}: {}", city.getCityName(), e.getMessage());
                }
            });
    
            log.info("Completed checking all cities at {}", LocalDateTime.now());
        }

    @Cacheable(cacheNames = "userAqiThresholds")
    public int getUserAqiThreshold(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return user.getAqiThreshold();
    }

    public FavoriteCity addFavoriteCity(Long userId, FavoriteCityDTO cityDTO) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (favoriteCityRepository.existsByUserIdAndLatitudeAndLongitude(
                userId, cityDTO.getLatitude(), cityDTO.getLongitude())) {
            throw new RuntimeException("Cette ville est déjà dans vos favoris");
        }

        FavoriteCity city = new FavoriteCity();
        city.setUser(user);
        city.setCityName(cityDTO.getCityName());
        city.setLatitude(cityDTO.getLatitude());
        city.setLongitude(cityDTO.getLongitude());
        city.setLastChecked(LocalDateTime.now());

        try {
            var pollution = pollutionService.getCurrentPollution(
                cityDTO.getLatitude(),
                cityDTO.getLongitude()
            );
            int currentAqi = pollution.getList().get(0).getMain().getAqi();
            city.setCurrentAqi(currentAqi);

            if (currentAqi >= user.getAqiThreshold()) {
                alertService.processAlert(user, city, currentAqi);
            }
        } catch (Exception e) {
            log.error("Error fetching initial AQI for city: {}", cityDTO.getCityName(), e);
        }

        return favoriteCityRepository.save(city);
    }

    public List<FavoriteCityDTO> getFavoriteCities(Long userId) {
        return favoriteCityRepository.findByUserId(userId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @CacheEvict(cacheNames = "userAqiThresholds")
    public void removeFavoriteCity(Long userId, Long cityId) {
        FavoriteCity city = favoriteCityRepository.findByIdAndUserId(cityId, userId)
            .orElseThrow(() -> new RuntimeException("Ville favorite non trouvée"));
        favoriteCityRepository.delete(city);
    }

    private FavoriteCityDTO convertToDTO(FavoriteCity city) {
        FavoriteCityDTO dto = new FavoriteCityDTO();
        dto.setId(city.getId());
        dto.setCityName(city.getCityName());
        dto.setLatitude(city.getLatitude());
        dto.setLongitude(city.getLongitude());
        dto.setCurrentAqi(city.getCurrentAqi());
        dto.setLastChecked(city.getLastChecked());
        return dto;
    }
}