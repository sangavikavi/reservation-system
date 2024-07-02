package com.hashedin.reservation.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hashedin.reservation.entity.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Restaurant findById(long id);
    List<Restaurant> findByLocation(String location);

    Object findByname(String name);

    List<Restaurant> findByManagerId(Long Id);
}
