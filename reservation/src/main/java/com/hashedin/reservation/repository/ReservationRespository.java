package com.hashedin.reservation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hashedin.reservation.entity.Reservation;
import com.hashedin.reservation.entity.ReservationRequest;


@Repository
public interface ReservationRespository extends JpaRepository<Reservation, Long> {
    
    Optional<Reservation> findById(Long id);

    List<Reservation> findByTableId(Long tableId);

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByRestaurantId(Long restaurantId);
   
}
