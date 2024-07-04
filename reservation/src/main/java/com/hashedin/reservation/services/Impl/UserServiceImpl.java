package com.hashedin.reservation.services.Impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hashedin.reservation.Config.SecurityUtil;
import com.hashedin.reservation.Dtos.RequestDtos.UserEntryDto;
import com.hashedin.reservation.entity.ReservationRequest;
import com.hashedin.reservation.entity.Restaurant;
import com.hashedin.reservation.entity.RestaurantUser;
import com.hashedin.reservation.services.UserService;
import com.hashedin.reservation.repository.ReservationRequestRepository;
import com.hashedin.reservation.repository.RestaurantRepository;
import com.hashedin.reservation.repository.UserRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the UserService interface.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRequestRepository reservationRequestRepository;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * Updates the user with the given information.
     *
     * @param user The user entry DTO containing the updated information.
     * @throws Exception If the user is not present.
     */
    @Override
    public void updateUser(UserEntryDto user) throws Exception {
        logger.info("Updating user: {}", user.getEmail());
        Optional<RestaurantUser> _user = userRepository.findByEmail(user.getEmail());
        if (!_user.isPresent()) {
            logger.error("User not present: {}", user.getEmail());
            throw new Exception("User not present please register");
        }
        _user.get().setEmail(user.getEmail());
        _user.get().setFullName(user.getFullName());
        _user.get().setPhoneNumber(user.getPhoneNumber());
        _user.get().setPassword(user.getPassword());
        logger.info("User updated successfully: {}", user.getEmail());
        userRepository.save(_user.get());
    }

    /**
     * Registers a new user with the given information and role.
     *
     * @param userDto The user entry DTO containing the user information.
     * @param role    The role of the user.
     * @return The registered user.
     * @throws Exception If there is an error during registration.
     */
    @Transactional
    @Override
    public RestaurantUser registerUser(UserEntryDto userDto, String role) throws Exception {
        logger.info("Registering user: {}", userDto.getEmail());
        // Create User entity and save
        // Before creating check if the user is already present
        RestaurantUser user = new RestaurantUser();
        user.setEmail(userDto.getEmail());
        user.setFullName(userDto.getFullName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        user.setRole(role);
        user.setCreatedAt(LocalDate.now());
        user.setUpdatedAt(LocalDate.now());
        logger.info("User registered successfully: {}", userDto.getEmail());
        return userRepository.save(user);
    }

    /**
     * Deletes the user with the given user ID.
     *
     * @param userId The ID of the user to be deleted.
     */
    @Override
    public void deleteUser(Long userId) {
        // Delete user only after checking if the user is present

    }

    /**
     * Retrieves the user with the given user ID.
     *
     * @param userId The ID of the user to be retrieved.
     * @return The user with the given ID.
     */
    @Override
    public RestaurantUser getUserById(Long userId) {
        // Get user only after checking if the user is present
        return userRepository.getById(userId);
    }

    /**
     * Retrieves the user with the given email.
     *
     * @param userId The email of the user to be retrieved.
     * @return The user with the given email.
     */
    @Override
    public RestaurantUser getUserByEmail(String userId) {
        // Get user only after checking if the user is present
        return userRepository.findByEmail(userId).get();
    }

    /**
     * Retrieves all users.
     *
     * @return The list of all users.
     */
    @Override
    public List<RestaurantUser> getAllUsers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllUsers'");
    }

    /**
     * Retrieves the reservations of the current user.
     *
     * @return The list of reservations of the current user.
     */
    public List<ReservationRequest> getReservations() {
        logger.info("Fetching user bookings");
        RestaurantUser user = (getUserByEmail(securityUtil.getCurrentUsername()));
        List<ReservationRequest> userReservations = reservationRequestRepository.findByUserId(user.getId());
        logger.info("User bookings fetched successfully");
        return userReservations;
    }

    /**
     * Retrieves the reservations of the restaurant with the given ID.
     *
     * @param id The ID of the restaurant.
     * @return The list of reservations of the restaurant.
     */
    public List<ReservationRequest> getRestaurantReservations(Long id) {
        logger.info("Fetching restaurant bookings");
        RestaurantUser user = (getUserByEmail(securityUtil.getCurrentUsername()));
        Restaurant restaurant = restaurantRepository.findById(id).get();
        List<ReservationRequest> restaurantReservations = reservationRequestRepository
                .findByRestaurantId(restaurant.getId());
        logger.info("Restaurant bookings fetched successfully");
        return restaurantReservations;
    }

    /**
     * Retrieves the restaurants managed by the current user.
     *
     * @return The list of restaurants managed by the current user.
     * @throws Exception If there is an error fetching the restaurants.
     */
    public List<Restaurant> getManagerRestaurants() throws Exception {
        try {

            logger.info("Fetching all restaurants");
            RestaurantUser user = (getUserByEmail(securityUtil.getCurrentUsername()));
            List<Restaurant> restaurants = restaurantRepository.findByManagerId(user.getId());
            logger.info("Fetched {} restaurants", restaurants.size());
            return restaurants;

        } catch (Exception e) {
            throw new Exception("Failed to fetch restaurants: " + e.getMessage());

        }

    }

}
