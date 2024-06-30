package com.hashedin.reservation.services.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.hashedin.reservation.entity.Reservation;
import com.hashedin.reservation.repository.ReservationRespository;
import com.hashedin.reservation.services.ReservationService;

public class ReservationServiceImpl implements ReservationService{

    @Autowired
    private ReservationRespository reservationRepository;

    @Override
    public void createReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Override
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).get();
    }

    @Override
    public List<Reservation> getAllReservations() {
       return reservationRepository.findAll();
    }

    @Override
    public void updateReservation(Reservation reservation) {
        Optional<Reservation> reservationData = reservationRepository.findById(reservation.getId());
        if(reservationData.isPresent()){
            Reservation _reservation = reservationData.get();
            _reservation.setReservationDate(reservation.getReservationDate());
            _reservation.setSlotStartTime(reservation.getSlotStartTime());
            _reservation.setSlotEndTime(reservation.getSlotEndTime());
            _reservation.setStatus(reservation.getStatus());
            _reservation.setUpdatedAt(reservation.getUpdatedAt());
            reservationRepository.save(_reservation);
        }
        reservationRepository.save(reservation);
        
    }

    @Override
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);        
    }
    
}
