package com.ftgo.restaurant.restaurantservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRestaurantDto {
    
    @NotBlank(message = "Restaurant name is required")
    private String name;
    
    @NotBlank(message = "Restaurant address is required")
    private String address;
    
    @NotNull(message = "Latitude is required")
    private Double latitude;
    
    @NotNull(message = "Longitude is required")
    private Double longitude;
    
    private String phone;
    
    private String cuisineType;
}