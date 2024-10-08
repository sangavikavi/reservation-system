package com.hashedin.reservation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hashedin.reservation.entity.RestaurantUser;



@Repository
public interface UserRepository extends JpaRepository<RestaurantUser, Long> {
        
        Optional<RestaurantUser> findByEmail(String email);


    
}
