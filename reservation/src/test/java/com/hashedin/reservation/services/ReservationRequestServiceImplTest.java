package com.hashedin.reservation.services;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hashedin.reservation.entity.Reservation;
import com.hashedin.reservation.entity.ReservationRequest;
import com.hashedin.reservation.repository.ReservationRequestRepository;
import com.hashedin.reservation.repository.ReservationRespository;
import com.hashedin.reservation.services.Impl.ReservationRequestServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationRequestServiceImplTest {

    @Mock
    private ReservationRequestRepository reservationRequestRepository;

    @Mock
    private ReservationRespository reservationRepository; 

    @InjectMocks
    private ReservationRequestServiceImpl reservationRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCancelReservation_ReservationFoundAndPending_CancelsReservation() throws Exception {
        // Arrange
        Long reservationRequestId = 1L;
        ReservationRequest reservationRequest = new ReservationRequest();
        Reservation reservation = new Reservation();
        reservation.setStatus("PENDING");
        reservationRequest.setReservation(reservation);

        when(reservationRequestRepository.findById(reservationRequestId)).thenReturn(Optional.of(reservationRequest));

        // Act
        reservationRequestService.cancelReservation(reservationRequestId);

        // Assert
        assertEquals("CANCELLED", reservation.getStatus());
        verify(reservationRequestRepository, times(1)).findById(reservationRequestId);
        verify(reservationRepository, times(1)).delete(reservation);
        verifyNoMoreInteractions(reservationRequestRepository, reservationRepository);
    }

    @Test
    void testCancelReservation_ReservationFoundAndConfirmed_PendsReservationForCancellation() throws Exception {
        // Arrange
        Long reservationRequestId = 1L;
        ReservationRequest reservationRequest = new ReservationRequest();
        Reservation reservation = new Reservation();
        reservation.setStatus("CONFIRMED");
        reservationRequest.setReservation(reservation);

        when(reservationRequestRepository.findById(reservationRequestId)).thenReturn(Optional.of(reservationRequest));

        // Act
        reservationRequestService.cancelReservation(reservationRequestId);

        // Assert
        assertEquals("PENDINGFORCANCELLATION", reservation.getStatus());
        verify(reservationRequestRepository, times(1)).findById(reservationRequestId);
        verify(reservationRepository, times(1)).save(reservation);
        verifyNoMoreInteractions(reservationRequestRepository, reservationRepository);
    }

    @Test
    void testCancelReservation_ReservationNotFound_ThrowsException() {
        // Arrange
        Long reservationRequestId = 1L;

        when(reservationRequestRepository.findById(reservationRequestId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(Exception.class, () -> reservationRequestService.cancelReservation(reservationRequestId));
        verify(reservationRequestRepository, times(1)).findById(reservationRequestId);
        verifyNoMoreInteractions(reservationRequestRepository, reservationRepository);
    }
}