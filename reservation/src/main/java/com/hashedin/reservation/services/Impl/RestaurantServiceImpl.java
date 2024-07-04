package com.hashedin.reservation.services.Impl;

import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.hashedin.reservation.Config.SecurityUtil;
import com.hashedin.reservation.Dtos.RequestDtos.RestaurantDto;
import com.hashedin.reservation.Dtos.ResponseDto.RestaurantResponseDto;
import com.hashedin.reservation.entity.Restaurant;
import com.hashedin.reservation.entity.RestaurantTable;
import com.hashedin.reservation.entity.RestaurantUser;
import com.hashedin.reservation.repository.RestaurantRepository;
import com.hashedin.reservation.services.RestaurantService;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Slf4j
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

    private static final Logger logger = LoggerFactory.getLogger(RestaurantServiceImpl.class);

    /**
     * Creates a new restaurant based on the provided restaurant data.
     *
     * @param restaurant The restaurant data to create a new restaurant.
     * @return The newly created restaurant.
     * @throws Exception If the restaurant already exists with the same name.
     */
    @Override
    public Restaurant createRestaurant(RestaurantDto restaurant) throws Exception {
        logger.info("Attempting to create restaurant: {}", restaurant.getName());
        if (restaurantRepository.findByname(restaurant.getName()) != null) {
            logger.warn("Restaurant already exists: {}", restaurant.getName());
            throw new Exception("Restaurant already exists. Please try with a different name");
        }    
        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setName(restaurant.getName());
        newRestaurant.setLocation(restaurant.getLocation());
        newRestaurant.setCuisines(restaurant.getCuisines());
        RestaurantUser manager = uService.getUserByEmail(securityUtil.getCurrentUsername());
        newRestaurant.setManager(manager);
        newRestaurant.setWorkingDays(restaurant.getWorkingDays());
        newRestaurant.setOpeningTime(restaurant.getOpeningTime());
        newRestaurant.setClosingTime(restaurant.getClosingTime());
        newRestaurant.setCreatedAt(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        newRestaurant.setUpdatedAt(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        Restaurant savedRestaurant = restaurantRepository.save(newRestaurant);
        logger.info("Restaurant created successfully: {}", savedRestaurant.getName());
        return savedRestaurant;
    }
    
    /**
     * Retrieves a restaurant by its ID.
     *
     * @param id The ID of the restaurant to retrieve.
     * @return The RestaurantResponseDto object containing the restaurant and its tables.
     * @throws Exception If the restaurant is not found.
     */
    @Override
    public RestaurantResponseDto getRestaurantById(Long id) throws Exception {
        logger.info("Fetching restaurant by id: {}", id);
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        List<RestaurantTable> tables = restaurantTableService.getTablesByRestaurantId(id);
        if (restaurant.isPresent()) {
            RestaurantResponseDto response = new RestaurantResponseDto();
            response.setRestaurant(restaurant.get());
            response.setTables(tables);
            logger.info("Restaurant fetched successfully: {}", id);
            return response;
        }
        logger.error("Restaurant not found: {}", id);
        throw new Exception("Restaurant not found");
    }

    /**
        * Retrieves a list of all restaurants.
        *
        * @return a list of Restaurant objects representing all the restaurants
        */
    @Override
    public Page<Restaurant> getAllRestaurants(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return restaurantRepository.findAll(pageable);
    }


    /**
        * Updates a restaurant with the given ID.
        *
        * @param id         The ID of the restaurant to update.
        * @param restaurant The updated restaurant object.
        * @return The updated restaurant object if the restaurant is found and updated successfully, null otherwise.
        */
    @Override
    public Restaurant updateRestaurant(Long id, Restaurant restaurant) {
        logger.info("Updating restaurant: {}", id);
        Optional<Restaurant> restaurantData = restaurantRepository.findById(id);
        if (restaurantData.isPresent()) {
            Restaurant _restaurant = restaurantData.get();
            _restaurant.setName(restaurant.getName());
            _restaurant.setLocation(restaurant.getLocation());
            _restaurant.setCuisines(restaurant.getCuisines());
            Restaurant updatedRestaurant = restaurantRepository.save(_restaurant);
            logger.info("Restaurant updated successfully: {}", id);
            return updatedRestaurant;
        }
        logger.error("Restaurant not found for update: {}", id);
        return null;
    }

    /**
        * Deletes a restaurant with the specified ID.
        *
        * @param id the ID of the restaurant to be deleted
        */
    @Override
    public void deleteRestaurant(Long id) {
        logger.info("Deleting restaurant: {}", id);
        restaurantRepository.deleteById(id);
        logger.info("Restaurant deleted successfully: {}", id);
    }
}
