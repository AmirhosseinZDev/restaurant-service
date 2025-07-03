package com.ftgo.restaurant.restaurantservice.controller;

import com.ftgo.restaurant.restaurantservice.dto.RestaurantDto;
import com.ftgo.restaurant.restaurantservice.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    
    private final RestaurantService restaurantService;
    
    @GetMapping("/near")
    public ResponseEntity<List<RestaurantDto>> getNearRestaurants(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "5.0") Double radius) {
        
        List<RestaurantDto> nearbyRestaurants = restaurantService.getNearRestaurants(
                latitude, longitude, radius);
        
        return ResponseEntity.ok(nearbyRestaurants);
    }
}