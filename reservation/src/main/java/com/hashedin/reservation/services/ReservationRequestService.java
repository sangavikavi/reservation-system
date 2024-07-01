
package com.hashedin.reservation.services;

import com.hashedin.reservation.Dtos.RequestDtos.ReservationRequestDto;
import com.hashedin.reservation.entity.Reservation;
import com.hashedin.reservation.entity.ReservationRequest;

import java.util.List;

public interface ReservationRequestService {

    ReservationRequest createReservation(ReservationRequestDto reservationRequest) throws Exception;

    ReservationRequest getReservationById(Long id);

    List<ReservationRequest> getAllReservations();

    ReservationRequest updateReservation(ReservationRequest reservation);

    void cancelReservation(Long id) throws Exception;

} 