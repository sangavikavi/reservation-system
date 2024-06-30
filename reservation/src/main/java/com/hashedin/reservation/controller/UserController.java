package com.hashedin.reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.hashedin.reservation.entity.RestaurantUser;
import com.hashedin.reservation.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

@RestController

public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("user/login")
    public String loginUser(@RequestBody RestaurantUser user) {
        // Logic for user login
        return "User login successful";
    }

    @PostMapping("user/register")
    public String registerUser(@RequestBody RestaurantUser user) {
        // Logic for user registration
        return "User registration successful";
    }

    @PostMapping("manager/login")
    public String loginManager(@RequestBody RestaurantUser user) {
        // Logic for manager login
        return "Manager login successful";
    }

    @PostMapping("manager/register")
    public String registerManager(@RequestBody RestaurantUser user) {
        // Logic for manager registration
        return "Manager registration successful";
    }
}


