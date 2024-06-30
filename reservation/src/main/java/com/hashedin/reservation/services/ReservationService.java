package com.hashedin.reservation.services;

import java.util.List;

import com.hashedin.reservation.entity.Reservation;

public interface ReservationService {
    
    void createReservation(Reservation reservation);
    
    Reservation getReservationById(Long id);
    
    List<Reservation> getAllReservations();
    
    void updateReservation(Reservation reservation);
    
    void deleteReservation(Long id);
}
