package com.ftgo.restaurant.restaurantservice.repository;

import com.ftgo.restaurant.restaurantservice.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    
    @Query("SELECT r FROM Restaurant r")
    List<Restaurant> findAll();
    
    // Note: In a real implementation, you would use a spatial database function
    // For simplicity, we'll fetch all restaurants and filter in the service layer
    @Query("SELECT r FROM Restaurant r WHERE " +
           "(:latitude - r.latitude) * (:latitude - r.latitude) + " +
           "(:longitude - r.longitude) * (:longitude - r.longitude) <= :radiusSquared")
    List<Restaurant> findRestaurantsWithinRadius(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radiusSquared") Double radiusSquared);
}