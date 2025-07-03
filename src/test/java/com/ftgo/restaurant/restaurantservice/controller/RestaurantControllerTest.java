package com.ftgo.restaurant.restaurantservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

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