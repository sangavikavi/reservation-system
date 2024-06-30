package com.hashedin.reservation.services.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hashedin.reservation.entity.RestaurantUser;
import com.hashedin.reservation.services.UserService;
import com.hashedin.reservation.repository.UserRepository;
import java.util.Optional;

public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository UserRepository;

    @Override
    public void createUser(RestaurantUser user) throws Exception {
        // Create user if the users email is not present early
        
        Optional<RestaurantUser> _user = UserRepository.findByEmail(user.getEmail());
        if (_user.isPresent()){
            throw new Exception("User already exists Please login");
        }
        UserRepository.save(user);
    }

    @Override
    public void updateUser(RestaurantUser user) throws Exception {
        Optional<RestaurantUser> _user = UserRepository.findByEmail(user.getEmail());
        if (!_user.isPresent()){
            throw new Exception("User not present please register");
        }
       _user.get().setEmail(user.getEmail());
       _user.get().setFullName(user.getFullName());
       _user.get().setPhoneNumber(user.getPhoneNumber());
       _user.get().setPassword(user.getPassword());
       UserRepository.save(_user.get());   
    }

    @Override
    public void deleteUser(Long userId) {
        //  Delete user only after checking if the user is present

    }

    @Override
    public RestaurantUser getUserById(Long userId) {
        //  Get user only after checking if the user is present
        return UserRepository.getById(userId);
    }

    @Override
    public List<RestaurantUser> getAllUsers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllUsers'");
    }
    
    
}
