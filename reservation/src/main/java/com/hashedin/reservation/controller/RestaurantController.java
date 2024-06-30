package com.hashedin.reservation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hashedin.reservation.entity.Restaurant;
import com.hashedin.reservation.repository.RestaurantRepository;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.getAllRestaurants();
    }

    @GetMapping("/{id}")
    public Restaurant getRestaurantById(@PathVariable Long id) {
        return restaurantRepository.getRestaurantById(id);
    }

    @PostMapping("/addNew")
    public Restaurant createRestaurant(@RequestBody Restaurant restaurant) {
        return restaurantRepository.createRestaurant(restaurant);
    }

    @PutMapping("/{id}")
    public Restaurant updateRestaurant(@PathVariable Long id, @RequestBody Restaurant restaurant) {
        return restaurantRepository.updateRestaurant(id, restaurant);
    }

    @DeleteMapping("/{id}")

    public void deleteRestaurant(@PathVariable Long id) {
        // Check if the restaurant is booked by any user
        if (isRestaurantBooked(id)) {
            throw new RuntimeException("Cannot delete the restaurant as it is booked by a user.");
        }

        restaurantRepository.deleteRestaurant(id);
    }

    private boolean isRestaurantBooked(Long id) {
        // Implement the logic to check if the restaurant is booked by any user
        // You can use the restaurant ID to query the bookings table or any other
        // relevant data source
        // Return true if the restaurant is booked, false otherwise
        // Example:
        // return bookingRepository.isRestaurantBooked(id);
        return true;
    }

}
