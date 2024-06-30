package com.hashedin.reservation.services;

import java.util.List;

import com.hashedin.reservation.entity.RestaurantUser;

public interface UserService {
    void createUser(RestaurantUser user) throws Exception;
    void updateUser(RestaurantUser user) throws Exception;
    void deleteUser(Long userId);
    RestaurantUser getUserById(Long userId);
    List<RestaurantUser> getAllUsers();
    
}
