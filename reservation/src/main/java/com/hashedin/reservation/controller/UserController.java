package com.hashedin.reservation.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.hashedin.reservation.Config.SecurityUtil;
import com.hashedin.reservation.Dtos.RequestDtos.UserEntryDto;
import com.hashedin.reservation.entity.JwtRequest;
import com.hashedin.reservation.entity.JwtResponse;
import com.hashedin.reservation.entity.RestaurantUser;
import com.hashedin.reservation.repository.UserRepository;
import com.hashedin.reservation.security.JWTHelper;
import com.hashedin.reservation.services.Impl.UserServiceImpl;
import io.swagger.v3.oas.models.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The UserController class is responsible for handling HTTP requests related to user operations.
 * It provides endpoints for user login, registration, and retrieving user bookings.
 */
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTHelper helper;

    @Autowired
    private SecurityUtil securityUtil;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    
    /**
     * Authenticates the user and generates a JWT token for successful login.
     *
     * @param request The JwtRequest object containing the user's email and password.
     * @return ResponseEntity containing the JWT token and the user's username if login is successful,
     *         or an error response if login fails.
     */
    @PostMapping("user/login")
    public ResponseEntity<?> loginUser(@RequestBody JwtRequest request) {
        logger.info("User login attempt: {}", request.getEmail());
        try {
            this.doAuthenticate(request.getEmail(), request.getPassword());
            RestaurantUser user = userRepository.findByEmail(request.getEmail()).get();
            if (!user.getRole().equals("USER")) {
                logger.warn("Unauthorized access attempt by user: {}", request.getEmail());
                return new ResponseEntity<>(ResponseEntity.status(401), HttpStatus.UNAUTHORIZED);
            }
            UserDetails userDetails = User.builder().username(user.getEmail()).password(user.getPassword())
                    .roles(user.getRole()).build();
            String token = this.helper.generateToken(userDetails);
            JwtResponse response = JwtResponse.builder()
                    .jwtToken(token)
                    .username(userDetails.getUsername()).build();
            logger.info("User login successful: {}", request.getEmail());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("User login failed: {}", e.getMessage());
            return new ResponseEntity<>(ResponseEntity.status(400), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Registers a new user.
     *
     * @param user The UserEntryDto object containing the user's details.
     * @return ResponseEntity containing a success message if registration is successful,
     *         or an error response if registration fails.
     */
    @PostMapping("user/register")
    public ResponseEntity<?> registerUser(@RequestBody UserEntryDto user) {
        logger.info("User registration attempt: {}", user.getEmail());
        try {
            RestaurantUser _user = userService.registerUser(user, "USER");
            logger.info("User registered successfully: {}", user.getEmail());
            return ResponseEntity.ok(new ApiResponse().description(user.getFullName() + " Registered Successfully"));
        } catch (Exception e) {
            logger.error("User registration failed: {}", e.getMessage());
            return new ResponseEntity<>(ResponseEntity.status(400), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Authenticates the manager and generates a JWT token for successful login.
     *
     * @param request The JwtRequest object containing the manager's email and password.
     * @return ResponseEntity containing the JWT token and the manager's username if login is successful,
     *         or an error response if login fails.
     */
    @PostMapping("manager/login")
    public ResponseEntity<?> loginManager(@RequestBody JwtRequest request) {
        logger.info("Manager login attempt: {}", request.getEmail());
        try {
            this.doAuthenticate(request.getEmail(), request.getPassword());
            RestaurantUser user = userRepository.findByEmail(request.getEmail()).get();
            if (!user.getRole().equals("MANAGER")) {
                logger.warn("Unauthorized access attempt by manager: {}", request.getEmail());
                return new ResponseEntity<>(ResponseEntity.status(401), HttpStatus.UNAUTHORIZED);
            }
            UserDetails userDetails = User.builder().username(user.getEmail()).password(user.getPassword())
                    .roles(user.getRole()).build();
            String token = this.helper.generateToken(userDetails);
            JwtResponse response = JwtResponse.builder()
                    .jwtToken(token)
                    .username(userDetails.getUsername()).build();
            logger.info("Manager login successful: {}", request.getEmail());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Manager login failed: {}", e.getMessage());
            return new ResponseEntity<>(ResponseEntity.status(400), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Registers a new manager.
     *
     * @param user The UserEntryDto object containing the manager's details.
     * @return ResponseEntity containing a success message if registration is successful,
     *         or an error response if registration fails.
     */
    @PostMapping("manager/register")
    public ResponseEntity<?> registerManager(@RequestBody UserEntryDto user) throws Exception {
        logger.info("Manager registration attempt: {}", user.getEmail());
        try {
            RestaurantUser _user = userService.registerUser(user, "MANAGER");
            logger.info("Manager registered successfully: {}", user.getEmail());
            return ResponseEntity.ok(new ApiResponse().description(user.getFullName() + " Registered Successfully"));
        } catch (Exception e) {
            logger.error("Manager registration failed: {}", e.getMessage());
            return new ResponseEntity<>(ResponseEntity.status(400), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Authenticates the user.
     *
     * @param email The user's email.
     * @param password The user's password.
     */
    private void doAuthenticate(String email, String password) {
        logger.info("Authenticating user: {}", email);
        Optional<RestaurantUser> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            logger.error("User not found: {}", email);
            throw new UsernameNotFoundException("User not found");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean isPasswordMatch = passwordEncoder.matches(password, user.get().getPassword());

        if (!isPasswordMatch) {
            logger.error("Invalid password for user: {}", email);
            throw new BadCredentialsException("Invalid password");
        }
        logger.info("User authenticated: {}", email);
    }

    
    /**
     * Retrieves the bookings made by the user.
     *
     * @return ResponseEntity containing the user's bookings if successful, or a bad request response with an error message if an exception occurs.
     */
    @GetMapping("user/mybookings")
    public ResponseEntity<?> getMyBookings() {
        logger.info("Fetching user bookings");
        try {
            return ResponseEntity.ok(userService.getReservations());
        } catch (Exception e) {
            logger.error("Failed to fetch user bookings: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    
    /**
     * Retrieves the bookings for a specific restaurant.
     *
     * @param id The ID of the restaurant.
     * @return A ResponseEntity containing the restaurant bookings.
     */
    @GetMapping("manager/restaurantbookings/{id}")
    public ResponseEntity<?> getRestaurantBookings(@PathVariable Long id) {
        logger.info("Fetching restaurant bookings");
        try {
            return ResponseEntity.ok(userService.getRestaurantReservations(id));
        } catch (Exception e) {
            logger.error("Failed to fetch restaurant bookings: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Retrieves all restaurants managed by the user.
     *
     * @return ResponseEntity containing the list of restaurants
     */
    @GetMapping("manager/restaurants")
    public ResponseEntity<?> getRestaurants() {
        logger.info("Fetching all restaurants");
        try {
            return ResponseEntity.ok(userService.getManagerRestaurants());
        } catch (Exception e) {
            logger.error("Failed to fetch restaurants: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Handles the exception thrown when the user provides bad credentials.
     *
     * @param e The BadCredentialsException that was thrown.
     * @return A string indicating that the credentials are invalid.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler(BadCredentialsException e) {
        logger.error("Bad credentials exception: {}", e.getMessage());
        return "Credentials Invalid !!";
    }

}
