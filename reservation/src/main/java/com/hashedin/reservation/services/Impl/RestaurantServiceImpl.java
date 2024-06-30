package com.hashedin.reservation.services.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.hashedin.reservation.entity.Restaurant;
import com.hashedin.reservation.repository.RestaurantRepository;
import com.hashedin.reservation.services.RestaurantService;

public class RestaurantServiceImpl implements RestaurantService{

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant getRestaurantById(Long id) {
       Optional<Restaurant> restaurant = restaurantRepository.findById(id);
       if(restaurant.isPresent()){
           return restaurant.get();
       }
       return null;
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant updateRestaurant(Long id, Restaurant restaurant) {
        Optional<Restaurant> restaurantData = restaurantRepository.findById(id);
        if(restaurantData.isPresent()){
            Restaurant _restaurant = restaurantData.get();
            _restaurant.setName(restaurant.getName());
            _restaurant.setLocation(restaurant.getLocation());
            _restaurant.setCuisines(restaurant.getCuisines());
            return restaurantRepository.save(_restaurant);
        }
        return null; 
    }

    @Override
    public void deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);
        
    }
    
}
