package com.hashedin.reservation.services;

import java.util.List;

import com.hashedin.reservation.Dtos.RequestDtos.UserEntryDto;
import com.hashedin.reservation.entity.RestaurantUser;

public interface UserService {
    RestaurantUser registerUser(UserEntryDto user,String role) throws Exception;
    void updateUser(UserEntryDto user) throws Exception;
    void deleteUser(Long userId);
    RestaurantUser getUserById(Long userId);
    RestaurantUser getUserByEmail(String email);
    List<RestaurantUser> getAllUsers();
    
}
