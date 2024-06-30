package com.hashedin.reservation.controller;

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

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantServiceImpl restaurantService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'MANAGER')")
    public ResponseEntity<?> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getRestaurantById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(restaurantService.getRestaurantById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/addNew")
    public ResponseEntity<?> createRestaurant(@RequestBody RestaurantDto restaurant) {
        try {
            return ResponseEntity.ok("Restaurant created successfully" + restaurantService.createRestaurant(restaurant));
        } catch (Exception e) {
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
