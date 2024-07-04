package com.hashedin.reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hashedin.reservation.Dtos.RequestDtos.ReservationRequestDto;
import com.hashedin.reservation.services.Impl.ReservationRequestServiceImpl;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@RestController
@RequestMapping("/reservations")
public class ReservationRequestController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationRequestController.class);

    @Autowired
    private ReservationRequestServiceImpl reservationService;

    /**
     * Endpoint to create a new reservation.
     *
     * @param reservationRequest The reservation request DTO.
     * @return ResponseEntity with the status of the reservation creation.
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createReservation(@RequestBody ReservationRequestDto reservationRequest) {
        try {
            logger.info("Creating reservation");
            reservationService.createReservation(reservationRequest);
            return ResponseEntity.ok("Reservation created successfully");
        } catch (Exception e) {
            logger.error("Failed to create reservation", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint to get all reservations for a specific restaurant.
     *
     * @param restaurantId The ID of the restaurant.
     * @return ResponseEntity with the list of reservations for the restaurant.
     */
    @GetMapping("/{restaurantId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> getAllReservationsbyRestaurant(@PathVariable Long restaurantId) {
        try {
            logger.info("Getting all reservations for restaurant with ID: {}", restaurantId);
            return ResponseEntity.ok(reservationService.getAllReservationsbyRestaurant(restaurantId));
        } catch (Exception e) {
            logger.error("Failed to get reservations for restaurant with ID: {}", restaurantId, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint to approve a reservation.
     *
     * @param reservationId The ID of the reservation to be approved.
     * @return ResponseEntity with the status of the reservation approval.
     */
    @PostMapping("/approve/{reservationId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> approveReservation(@PathVariable Long reservationId) {
        try {
            logger.info("Approving reservation with ID: {}", reservationId);
            reservationService.approveReservationRequest(reservationId);
            return ResponseEntity.ok("Reservation approved successfully");
        } catch (Exception e) {
            logger.error("Failed to approve reservation with ID: {}", reservationId, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint to reject a reservation.
     *
     * @param reservationId The ID of the reservation to be rejected.
     * @return ResponseEntity with the status of the reservation rejection.
     */
    @PostMapping("/reject/{reservationId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> rejectReservation(@PathVariable Long reservationId) {
        try {
            logger.info("Rejecting reservation with ID: {}", reservationId);
            reservationService.rejectReservationRequest(reservationId);
            return ResponseEntity.ok("Reservation rejected successfully");
        } catch (Exception e) {
            logger.error("Failed to reject reservation with ID: {}", reservationId, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint to accept a cancellation request for a reservation.
     *
     * @param reservationId The ID of the reservation for which the cancellation is accepted.
     * @return ResponseEntity with the status of the cancellation acceptance.
     */
    @PostMapping("/acceptcancellation/{reservationId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> acceptCancellation(@PathVariable Long reservationId) {
        try {
            logger.info("Accepting cancellation for reservation with ID: {}", reservationId);
            reservationService.approveCancellationRequest(reservationId);
            return ResponseEntity.ok("Cancellation accepted successfully");
        } catch (Exception e) {
            logger.error("Failed to accept cancellation for reservation with ID: {}", reservationId, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint to cancel a reservation.
     *
     * @param reservationId The ID of the reservation to be cancelled.
     * @return ResponseEntity with the status of the reservation cancellation.
     */
    @PostMapping("/cancel/{reservationId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> cancelReservation(@PathVariable Long reservationId) {
        try {
            logger.info("Cancelling reservation with ID: {}", reservationId);
            reservationService.cancelReservation(reservationId);
            return ResponseEntity.ok("Reservation Cancellation initiated successfully");
        } catch (Exception e) {
            logger.error("Failed to cancel reservation with ID: {}", reservationId, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint to get all reservations for a specific table.
     *
     * @param tableId The ID of the table.
     * @return ResponseEntity with the list of reservations for the table.
     */
    @PostMapping("table/{tableId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getReservationsByTable(@PathVariable Long tableId) {
        try {
            logger.info("Getting all reservations for table with ID: {}", tableId);
            return ResponseEntity.ok(reservationService.getReservationsbyTable(tableId));
        } catch (Exception e) {
            logger.error("Failed to get reservations for table with ID: {}", tableId, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
