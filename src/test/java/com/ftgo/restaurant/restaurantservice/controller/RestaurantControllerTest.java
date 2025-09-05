package com.ftgo.restaurant.restaurantservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftgo.restaurant.restaurantservice.dto.CreateRestaurantDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testCreateRestaurant() throws Exception {
        CreateRestaurantDto createDto = new CreateRestaurantDto(
                "Test Restaurant",
                "123 Test St, Test City",
                40.7580,
                -73.9855,
                "555-1234",
                "American"
        );

        mockMvc.perform(post("/api/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test Restaurant"))
                .andExpect(jsonPath("$.address").value("123 Test St, Test City"))
                .andExpect(jsonPath("$.latitude").value(40.7580))
                .andExpect(jsonPath("$.longitude").value(-73.9855))
                .andExpect(jsonPath("$.phone").value("555-1234"))
                .andExpect(jsonPath("$.cuisineType").value("American"));
    }

    @Test
    @WithMockUser
    void testCreateRestaurantWithInvalidData() throws Exception {
        CreateRestaurantDto createDto = new CreateRestaurantDto(
                "", // Empty name should fail validation
                "",
                null,
                null,
                "555-1234",
                "American"
        );

        mockMvc.perform(post("/api/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateRestaurantWithoutAuth() throws Exception {
        CreateRestaurantDto createDto = new CreateRestaurantDto(
                "Test Restaurant",
                "123 Test St, Test City",
                40.7580,
                -73.9855,
                "555-1234",
                "American"
        );

        mockMvc.perform(post("/api/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testGetNearRestaurants() throws Exception {
        mockMvc.perform(get("/api/restaurants/near")
                .param("latitude", "40.7580")
                .param("longitude", "-73.9855")
                .param("radius", "2.0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].distance").exists());
    }

    @Test
    void testGetNearRestaurantsWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/restaurants/near")
                .param("latitude", "40.7580")
                .param("longitude", "-73.9855"))
                .andExpect(status().isUnauthorized());
    }
}