
package com.hashedin.reservation.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.hashedin.reservation.Dtos.RequestDtos.RestaurantDto;
import com.hashedin.reservation.Dtos.ResponseDto.RestaurantResponseDto;
import com.hashedin.reservation.entity.Restaurant;

public interface RestaurantService {
    Restaurant createRestaurant(RestaurantDto restaurant) throws Exception;
    RestaurantResponseDto getRestaurantById(Long id) throws Exception;
    Page<Restaurant> getAllRestaurants(int page, int size);
    Restaurant updateRestaurant(Long id, Restaurant restaurant);
    void deleteRestaurant(Long id);
}
