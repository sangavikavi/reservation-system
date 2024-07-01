package com.hashedin.reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hashedin.reservation.Dtos.RequestDtos.ReservationRequestDto;
import com.hashedin.reservation.services.Impl.ReservationRequestServiceImpl;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/reservations")
public class ReservationRequestController {

    @Autowired
    private ReservationRequestServiceImpl reservationService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createReservation(@RequestBody ReservationRequestDto reservationRequest) {
        try {
            return ResponseEntity.ok("Reservation created successfully" + reservationService.createReservation(reservationRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
