package com.hashedin.reservation.services.Impl;

import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.beans.factory.annotation.Autowired;

import com.hashedin.reservation.entity.Reservation;
import com.hashedin.reservation.entity.ReservationRequest;
import com.hashedin.reservation.services.ReservationRequestService;
import com.hashedin.reservation.repository.ReservationRequestRepository;

public class ReservationRequestServiceImpl implements ReservationRequestService{

    @Autowired
    private ReservationRequestRepository reservationRequestRepository;

    @Override
    public ReservationRequest createReservation(ReservationRequest reservation) {
        // First check if the restratant is available (working )
        // If then check the requested table is available 
        // Check if the table can be fit in the given persons or strength 
        //  This needs to be checked in frontend itself
        return reservationRequestRepository.save(reservation);
    }

    @Override
    public ReservationRequest getReservationById(Long id) {
       Optional<ReservationRequest> reservation = reservationRequestRepository.findById(id);
         if(reservation.isPresent()){
              return reservation.get();
         }
            return null;
    }

    @Override
    public List<ReservationRequest> getAllReservations() {
        return reservationRequestRepository.findAll();
    }

    @Override
    public ReservationRequest updateReservation(ReservationRequest reservation) {
        Optional<ReservationRequest> reservationData = reservationRequestRepository.findById(reservation.getId());
        if(reservationData.isPresent()){
            ReservationRequest _reservation = reservationData.get();
            _reservation.setRequestDate(reservation.getRequestDate());
            _reservation.setSlotStartTime(reservation.getSlotStartTime());
            _reservation.setSlotEndTime(reservation.getSlotEndTime());
            _reservation.setStatus(reservation.getStatus());
            _reservation.setUpdatedAt(reservation.getUpdatedAt());
            reservationRequestRepository.save(_reservation);
        }
        return null;
    }

    @Override
    public void deleteReservation(Long id) {
        reservationRequestRepository.deleteById(id);
    }
    
    
    

 
    
}
