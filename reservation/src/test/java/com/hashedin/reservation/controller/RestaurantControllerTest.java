package com.hashedin.reservation.controller;

import com.hashedin.reservation.Dtos.RequestDtos.RestaurantDto;
import com.hashedin.reservation.services.Impl.RestaurantServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RestaurantControllerTest {

    @Mock
    private RestaurantServiceImpl restaurantService;

    @InjectMocks
    private RestaurantController restaurantController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRestaurants() {
        // TODO: Implement test case for getAllRestaurants method
    }

    @Test
    void testGetRestaurantById() {
        // TODO: Implement test case for getRestaurantById method
    }

    @Test
    void testCreateRestaurant() {
        // TODO: Implement test case for createRestaurant method
    }
}