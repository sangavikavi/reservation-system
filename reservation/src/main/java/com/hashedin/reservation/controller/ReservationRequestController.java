package com.hashedin.reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
            return ResponseEntity
                    .ok("Reservation created successfully" + reservationService.createReservation(reservationRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{restaurantId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> getAllReservationsbyRestaurant(@PathVariable Long restaurantId) {
        try {
            return ResponseEntity.ok(reservationService.getAllReservationsbyRestaurant(restaurantId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/approve/{reservationId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> approveReservation(@PathVariable Long reservationId) {
        try {
            reservationService.approveReservationRequest(reservationId);
            return ResponseEntity.ok("Reservation approved successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reject/{reservationId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> rejectReservation(@PathVariable Long reservationId) {
        try {
            reservationService.rejectReservationRequest(reservationId);
            return ResponseEntity.ok("Reservation rejected successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/acceptcancellation/{reservationId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> acceptCancellation(@PathVariable Long reservationId) {
        try {
            reservationService.approveCancellationRequest(reservationId);
            return ResponseEntity.ok("Cancellation accepted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/cancel/{reservationId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> cancelReservation(@PathVariable Long reservationId) {
        try {
            reservationService.cancelReservation(reservationId);
            return ResponseEntity.ok("Reservation Cancellation initiated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
