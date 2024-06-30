
package com.hashedin.reservation.services;

import com.hashedin.reservation.entity.Reservation;
import com.hashedin.reservation.entity.ReservationRequest;

import java.util.List;

public interface ReservationRequestService {

    ReservationRequest createReservation(ReservationRequest reservation);

    ReservationRequest getReservationById(Long id);

    List<ReservationRequest> getAllReservations();

    ReservationRequest updateReservation(ReservationRequest reservation);

    void deleteReservation(Long id);

} 