package com.hashedin.reservation.repository;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hashedin.reservation.entity.Reservation;
import com.hashedin.reservation.entity.ReservationRequest;


@Repository
public interface ReservationRespository extends JpaRepository<Reservation, Long> {
    
    Optional<Reservation> findById(Long id);

    List<Reservation> findByTableId(Long tableId);

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByRestaurantId(Long restaurantId);

    List<Reservation> findByReservationDate(Date requestDate);

    List<Reservation> findBySlotStartTime(LocalTime slotStartTime);

    List<Reservation> findBySlotEndTime(LocalTime slotEndTime);

    List<Reservation> findByStatusContainingIgnoreCase(String status);
   
}
