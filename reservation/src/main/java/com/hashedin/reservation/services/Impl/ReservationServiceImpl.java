package com.hashedin.reservation.services.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.hashedin.reservation.entity.Reservation;
import com.hashedin.reservation.repository.ReservationRespository;
import com.hashedin.reservation.services.ReservationService;

/**
 * Implementation class for the ReservationService interface.
 */
public class ReservationServiceImpl implements ReservationService{

    @Autowired
    private ReservationRespository reservationRepository;

    /**
     * Creates a new reservation.
     * @param reservation The reservation to be created.
     */
    @Override
    public void createReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    /**
     * Retrieves a reservation by its ID.
     * @param id The ID of the reservation.
     * @return The reservation with the specified ID.
     */
    @Override
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).get();
    }

    /**
     * Retrieves all reservations.
     * @return A list of all reservations.
     */
    @Override
    public List<Reservation> getAllReservations() {
       return reservationRepository.findAll();
    }

    /**
     * Updates a reservation.
     * @param reservation The updated reservation.
     */
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

    /**
     * Deletes a reservation by its ID.
     * @param id The ID of the reservation to be deleted.
     */
    @Override
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);        
    }
    
}
