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
    private UserServiceImpl UserService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTHelper helper;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private UserServiceImpl uService;

    @PostMapping("user/login")
    public ResponseEntity<?> loginUser(@RequestBody JwtRequest request) {
        this.doAuthenticate(request.getEmail(), request.getPassword());
        RestaurantUser user = userRepository.findByEmail(request.getEmail()).get();
        if (!user.getRole().equals("USER")) {
            return new ResponseEntity<>(ResponseEntity.status(401), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = User.builder().username(user.getEmail()).password(user.getPassword())
                .roles(user.getRole()).build();
        String token = this.helper.generateToken(userDetails);
        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .username(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("user/register")
    public ResponseEntity<?> registerUser(@RequestBody UserEntryDto user) {

        try {
            RestaurantUser _user = UserService.registerUser(user,"USER");
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseEntity.status(400), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse().description(user.getFullName() + "Registered Succesfully"));
    }

    @PostMapping("manager/login")
    public ResponseEntity<?> loginManager(@RequestBody JwtRequest request) {
        this.doAuthenticate(request.getEmail(), request.getPassword());
        RestaurantUser user = userRepository.findByEmail(request.getEmail()).get();
        if (!user.getRole().equals("MANAGER")){
            return new ResponseEntity<>(ResponseEntity.status(401), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = User.builder().username(user.getEmail()).password(user.getPassword())
                .roles(user.getRole()).build();
        String token = this.helper.generateToken(userDetails);
        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .username(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("manager/register")
    public ResponseEntity<?> registerManager(@RequestBody UserEntryDto user) throws Exception {
        try {
            RestaurantUser _user = UserService.registerUser(user,"MANAGER");
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseEntity.status(400), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse().description(user.getFullName() + "Registered Succesfully"));
    }

    private void doAuthenticate(String email, String password) {
        Optional<RestaurantUser> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean isPasswordMatch = passwordEncoder.matches(password, user.get().getPassword());

        if (!isPasswordMatch) {
            throw new BadCredentialsException("Invalid password");
        }

    }

    @PostMapping("user/mybookings")
    public ResponseEntity<?> getMyBookings() {
        try{
            return ResponseEntity.ok(uService.getReservations());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("manager/restaurantbookings")
    public ResponseEntity<?> getRestaurantBookings() {
        try {
            return ResponseEntity.ok(uService.getRestaurantReservations());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }

}
