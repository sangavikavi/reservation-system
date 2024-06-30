package com.hashedin.reservation.services.Impl;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hashedin.reservation.Dtos.RequestDtos.UserEntryDto;
import com.hashedin.reservation.entity.RestaurantUser;
import com.hashedin.reservation.services.UserService;
import com.hashedin.reservation.repository.UserRepository;
import java.util.Optional;

import javax.management.relation.RoleNotFoundException;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    

    @Override
    public void updateUser(UserEntryDto user) throws Exception {
        Optional<RestaurantUser> _user = userRepository.findByEmail(user.getEmail());
        if (!_user.isPresent()){
            throw new Exception("User not present please register");
        }
       _user.get().setEmail(user.getEmail());
       _user.get().setFullName(user.getFullName());
       _user.get().setPhoneNumber(user.getPhoneNumber());
       _user.get().setPassword(user.getPassword());
       userRepository.save(_user.get());   
    }

    @Transactional
    @Override
    public RestaurantUser registerUser(UserEntryDto userDto,String role) throws Exception{
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
        return userRepository.save(user);
    }


    @Override
    public void deleteUser(Long userId) {
        //  Delete user only after checking if the user is present

    }

    @Override
    public RestaurantUser getUserById(Long userId) {
        //  Get user only after checking if the user is present
        return userRepository.getById(userId);
    }
    
    @Override
    public RestaurantUser getUserByEmail(String userId) {
        // Get user only after checking if the user is present
        return userRepository.findByEmail(userId).get();
    }

    @Override
    public List<RestaurantUser> getAllUsers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllUsers'");
    }
    
    
}
