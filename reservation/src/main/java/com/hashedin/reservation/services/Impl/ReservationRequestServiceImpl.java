package com.hashedin.reservation.services.Impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.LocalDateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hashedin.reservation.Config.SecurityUtil;
import com.hashedin.reservation.Dtos.RequestDtos.ReservationRequestDto;
import com.hashedin.reservation.entity.Reservation;
import com.hashedin.reservation.entity.ReservationRequest;
import com.hashedin.reservation.entity.Restaurant;
import com.hashedin.reservation.entity.RestaurantTable;
import com.hashedin.reservation.entity.RestaurantUser;
import com.hashedin.reservation.services.ReservationRequestService;
import com.hashedin.reservation.repository.ReservationRequestRepository;
import com.hashedin.reservation.repository.ReservationRespository;
import com.hashedin.reservation.repository.RestaurantRepository;
import com.hashedin.reservation.repository.RestaurantTableRepository;

@Service
public class ReservationRequestServiceImpl implements ReservationRequestService {

    @Autowired
    private ReservationRespository reservationRepository;

    @Autowired
    private ReservationRequestRepository reservationRequestRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private UserServiceImpl uService;

    @Autowired
    private RestaurantTableRepository tableRepository;

    private static final Map<Integer, String> days = new HashMap<Integer, String>() {
        {
            put(0, "SUNDAY");
            put(1, "MONDAY");
            put(2, "TUESDAY");
            put(3, "WEDNESDAY");
            put(4, "THURSDAY");
            put(5, "FRIDAY");
            put(6, "SATURDAY");
        }
    };

    @Override
    public ReservationRequest createReservation(ReservationRequestDto reservation) throws Exception {
        // First check if the restratant is available (working )
        // If then check the requested table is available
        // Check if the table can be fit in the given persons or strength
        // This needs to be checked in frontend itself
        // If all the above conditions are satisfied then create the reservation
        // Else throw an exception

        Reservation newReservation = new Reservation();
        Restaurant restaurant = restaurantRepository.findById(reservation.getRestaurantId()).get();
        List<String> workingDays = restaurant.getWorkingDays();
        int requestedDay = reservation.getRequestDate().getDay();
        if (!workingDays.contains(days.get(requestedDay))) {
            throw new Exception("Restaurant is closed on the requested day");
        }
        if (restaurant.getCapacity() < reservation.getNumberOfGuests()) {
            throw new Exception("Restaurant does not have enough capacity");
        }
        if (restaurant.getOpeningTime().getHour() < reservation.getSlotStartTime().getHour()
                || restaurant.getClosingTime().getHour() > reservation.getSlotEndTime().getHour()) {
            throw new Exception("Restaurant is closed at the requested time");
        }

        newReservation.setReservationDate(reservation.getRequestDate());
        newReservation.setSlotStartTime(reservation.getSlotStartTime());
        newReservation.setSlotEndTime(reservation.getSlotEndTime());
        newReservation.setNumberOfGuests(reservation.getNumberOfGuests());
        newReservation.setCreatedAt(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        newReservation.setUpdatedAt(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        newReservation.setRestaurant(restaurant);
        RestaurantUser user = (uService.getUserByEmail(securityUtil.getCurrentUsername()));
        newReservation.setUser(user);
        newReservation.setStatus("PENDING");
        RestaurantTable table = tableRepository.findById(reservation.getTableId()).get();
        if (table.getCapacity() < reservation.getNumberOfGuests()) {
            throw new Exception("Table does not have enough capacity Please book a bigger table");
        }
        newReservation.setTable(table);
        ReservationRequest reservationRequest = new ReservationRequest();
        // reservationRequest.setRequestDate(reservation.getRequestDate());
        // reservationRequest.setSlotStartTime(reservation.getSlotStartTime());
        // reservationRequest.setSlotEndTime(reservation.getSlotEndTime());
        // reservationRequest.setStatus("PENDING");
        reservationRequest.setTableId(reservation.getTableId());
        reservationRequest.setRestaurantId(reservation.getRestaurantId());
        reservationRequest.setUserId(user.getId());
        reservationRequest.setCreatedAt(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        reservationRequest.setUpdatedAt(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        reservationRequest.setReservation(newReservation);
        return reservationRequestRepository.save(reservationRequest);
    }

    public List<ReservationRequest> getReservationsbyTable(Long tableId) {
        return reservationRequestRepository.findByTableId(tableId);
    }

    @Override
    public ReservationRequest getReservationById(Long id) {
        Optional<ReservationRequest> reservation = reservationRequestRepository.findById(id);
        if (reservation.isPresent()) {
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
        if (reservationData.isPresent()) {
            ReservationRequest _reservation = reservationData.get();
            // _reservation.setRequestDate(reservation.getRequestDate());
            // _reservation.setSlotStartTime(reservation.getSlotStartTime());
            // _reservation.setSlotEndTime(reservation.getSlotEndTime());
            // _reservation.setStatus(reservation.getStatus());
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
