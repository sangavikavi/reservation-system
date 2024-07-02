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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @PostMapping("user/mybookings")
    public ResponseEntity<?> getMyBookings() {
        logger.info("Fetching user bookings");
        try {
            return ResponseEntity.ok(userService.getReservations());
        } catch (Exception e) {
            logger.error("Failed to fetch user bookings: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("manager/restaurantbookings")
    public ResponseEntity<?> getRestaurantBookings() {
        logger.info("Fetching restaurant bookings");
        try {
            return ResponseEntity.ok(userService.getRestaurantReservations());
        } catch (Exception e) {
            logger.error("Failed to fetch restaurant bookings: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler(BadCredentialsException e) {
        logger.error("Bad credentials exception: {}", e.getMessage());
        return "Credentials Invalid !!";
    }
}
