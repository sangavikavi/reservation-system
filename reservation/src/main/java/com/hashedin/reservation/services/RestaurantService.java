
package com.hashedin.reservation.services;

import java.util.List;

import com.hashedin.reservation.Dtos.RequestDtos.RestaurantDto;
import com.hashedin.reservation.Dtos.ResponseDto.RestaurantResponseDto;
import com.hashedin.reservation.entity.Restaurant;

public interface RestaurantService {
    Restaurant createRestaurant(RestaurantDto restaurant) throws Exception;
    RestaurantResponseDto getRestaurantById(Long id) throws Exception;
    List<Restaurant> getAllRestaurants();
    Restaurant updateRestaurant(Long id, Restaurant restaurant);
    void deleteRestaurant(Long id);
}
