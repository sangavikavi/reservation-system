package com.hashedin.reservation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hashedin.reservation.Dtos.RequestDtos.RestaurantDto;
import com.hashedin.reservation.services.Impl.RestaurantServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final Logger logger = LoggerFactory.getLogger(RestaurantController.class);

    @Autowired
    private RestaurantServiceImpl restaurantService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'MANAGER')")
    public ResponseEntity<?> getAllRestaurants() {
        logger.info("GET request received for all restaurants");
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getRestaurantById(@PathVariable Long id) {
        try {
            logger.info("GET request received for restaurant with id: {}", id);
            return ResponseEntity.ok(restaurantService.getRestaurantById(id));
        } catch (Exception e) {
            logger.error("Error occurred while getting restaurant with id: {}", id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/addNew")
    public ResponseEntity<?> createRestaurant(@RequestBody RestaurantDto restaurant) {
        try {
            logger.info("POST request received to create a new restaurant");
            return ResponseEntity.ok("Restaurant created successfully" + restaurantService.createRestaurant(restaurant));
        } catch (Exception e) {
            logger.error("Error occurred while creating a new restaurant", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }  
    }

    // @PutMapping("/{id}")
    // public Restaurant updateRestaurant(@PathVariable Long id, @RequestBody Restaurant restaurant) {
    //     return restaurantService.updateRestaurant(id, restaurant);  
    // }

    // @DeleteMapping("/{id}")

    // public void deleteRestaurant(@PathVariable Long id) {
    //     // Check if the restaurant is booked by any user
    //     // All the things will be handled by the service layer
    //     restaurantService.deleteRestaurant(id);
    // }

}
