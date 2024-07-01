package com.hashedin.reservation.services.Impl;

import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hashedin.reservation.Config.SecurityUtil;
import com.hashedin.reservation.Dtos.RequestDtos.RestaurantDto;
import com.hashedin.reservation.Dtos.ResponseDto.RestaurantResponseDto;
import com.hashedin.reservation.entity.Restaurant;
import com.hashedin.reservation.entity.RestaurantTable;
import com.hashedin.reservation.entity.RestaurantUser;
import com.hashedin.reservation.repository.RestaurantRepository;
import com.hashedin.reservation.services.RestaurantService;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantTableServiceImpl restaurantTableService;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private UserServiceImpl uService;

    @Override
    public Restaurant createRestaurant(RestaurantDto restaurant) throws Exception {
        if (restaurantRepository.findByname(restaurant.getName()) != null) {
            throw new Exception("Restaurant already exists Please try with different name");
        }
        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setName(restaurant.getName());
        newRestaurant.setLocation(restaurant.getLocation());
        newRestaurant.setCuisines(restaurant.getCuisines());
        RestaurantUser manager = (uService.getUserByEmail(securityUtil.getCurrentUsername()));
        newRestaurant.setManager(manager);
        newRestaurant.setWorkingDays(restaurant.getWorkingDays());
        newRestaurant.setOpeningTime(restaurant.getOpeningTime());
        newRestaurant.setClosingTime(restaurant.getClosingTime());
        newRestaurant.setCreatedAt(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        newRestaurant.setUpdatedAt(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        return restaurantRepository.save(newRestaurant);
    }

    @Override
    public RestaurantResponseDto getRestaurantById(Long id) throws Exception {
        // If the user is requesting for the restaurant Give the tables of the
        // restaurant
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        List<RestaurantTable> tables = restaurantTableService.getTablesByRestaurantId(id);
        if (restaurant.isPresent()) {
            RestaurantResponseDto response = new RestaurantResponseDto();
            response.setRestaurant(restaurant.get());
            response.setTables(tables);
            return response;
        }
        throw new Exception("Restaurant not found");
    }


    // public Restaurant getRestaurantByManager(RestaurantUser user) {
    // Optional<Restaurant> restaurant = restaurantRepository.findById(id);
    // if (restaurant.isPresent()) {
    // return restaurant.get();
    // }
    // return null;
    // }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant updateRestaurant(Long id, Restaurant restaurant) {
        Optional<Restaurant> restaurantData = restaurantRepository.findById(id);
        if (restaurantData.isPresent()) {
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
