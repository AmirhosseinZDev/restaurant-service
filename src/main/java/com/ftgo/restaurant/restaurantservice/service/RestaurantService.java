package com.ftgo.restaurant.restaurantservice.service;

import com.ftgo.restaurant.restaurantservice.dto.RestaurantDto;
import com.ftgo.restaurant.restaurantservice.model.Restaurant;
import com.ftgo.restaurant.restaurantservice.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    
    private final RestaurantRepository restaurantRepository;
    
    public List<RestaurantDto> getNearRestaurants(Double userLatitude, Double userLongitude, Double radiusKm) {
        List<Restaurant> allRestaurants = restaurantRepository.findAll();
        
        return allRestaurants.stream()
                .map(restaurant -> {
                    double distance = calculateDistance(userLatitude, userLongitude, 
                                                      restaurant.getLatitude(), restaurant.getLongitude());
                    return new RestaurantDto(
                            restaurant.getId(),
                            restaurant.getName(),
                            restaurant.getAddress(),
                            restaurant.getLatitude(),
                            restaurant.getLongitude(),
                            restaurant.getPhone(),
                            restaurant.getCuisineType(),
                            distance
                    );
                })
                .filter(dto -> dto.getDistance() <= radiusKm)
                .sorted((a, b) -> Double.compare(a.getDistance(), b.getDistance()))
                .collect(Collectors.toList());
    }
    
    /**
     * Calculate distance between two points using Haversine formula
     * Returns distance in kilometers
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // Distance in km
        
        return Math.round(distance * 100.0) / 100.0; // Round to 2 decimal places
    }
}