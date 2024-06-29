package com.hashedin.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hashedin.reservation.entity.User;

import java.util.Optional;

@Repository
public interface ResturantTableRepository extends JpaRepository<User, Long> {
   
}

