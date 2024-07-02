package com.hashedin.reservation.services.Impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

import lombok.extern.slf4j.Slf4j;

import com.hashedin.reservation.repository.ReservationRequestRepository;
import com.hashedin.reservation.repository.ReservationRespository;
import com.hashedin.reservation.repository.RestaurantRepository;
import com.hashedin.reservation.repository.RestaurantTableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the ReservationRequestService interface and provides
 * the implementation for creating, updating, and canceling reservations.
 * It also provides methods to retrieve reservations by table, by ID, and for
 * getting all reservations.
 */

@Slf4j
@Service
public class ReservationRequestServiceImpl implements ReservationRequestService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationRequestServiceImpl.class);

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
        LocalDate requestedDate = reservation.getRequestDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        logger.info("Creating reservation");
        if (isTableAlreadyReserved(reservation.getTableId(), reservation.getRequestDate(),
                reservation.getSlotStartTime(), reservation.getSlotEndTime())) {
            logger.error("Table already reserved");
            throw new Exception("Table already reserved");
        }

        LocalDate currentDate = LocalDate.now();
        if (requestedDate.isBefore(currentDate)) {
            logger.error("Requested date is in the past");
            throw new Exception("Requested date is in the past");
        }

        Reservation newReservation = new Reservation();
        Restaurant restaurant = restaurantRepository.findById(reservation.getRestaurantId()).get();
        List<String> workingDays = restaurant.getWorkingDays();
        int requestedDay = reservation.getRequestDate().getDay();
        workingDays.replaceAll(String::toUpperCase);
        if (!workingDays.contains(days.get(requestedDay))) {
            logger.error("Restaurant is closed on the requested day");
            throw new Exception("Restaurant is closed on the requested day");
        }
        // if (restaurant.getCapacity() < reservation.getNumberOfGuests()) {
        // logger.error("Restaurant does not have enough capacity");
        // throw new Exception("Restaurant does not have enough capacity");
        // }
        if (restaurant.getOpeningTime().getHour() > reservation.getSlotStartTime().getHour()
                || restaurant.getClosingTime().getHour() < reservation.getSlotEndTime().getHour()) {
            logger.error("Restaurant is closed at the requested time");
            throw new Exception("Restaurant is closed at the requested time");
        }

        newReservation.setReservationDate(requestedDate);
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
            logger.error("Table does not have enough capacity");
            throw new Exception("Table does not have enough capacity Please book a bigger table");
        }
        newReservation.setTable(table);
        reservationRepository.save(newReservation);
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
        logger.info("Reservation created successfully");
        return reservationRequestRepository.save(reservationRequest);
    }

    private boolean isTableAlreadyReserved(Long tableId, Date requestDate, LocalTime slotStartTime,
            LocalTime slotEndTime) {
        List<Reservation> allReservations = reservationRepository.findAll();

        List<Reservation> filteredReservations = allReservations.stream()
                .filter(reservation -> reservation.getTable().getId().equals(tableId))
                .filter(reservation -> reservation.getReservationDate()
                        .equals(requestDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()))
                .filter(reservation -> reservation.getSlotStartTime().equals(slotStartTime))
                .filter(reservation -> reservation.getSlotEndTime().equals(slotEndTime))
                .filter(reservation -> !reservation.getStatus().toLowerCase().contains("cancel"))
                .collect(Collectors.toList());

        return !filteredReservations.isEmpty();

    }

    public List<ReservationRequest> getReservationsbyTable(Long tableId) throws Exception {

        try {
            logger.info("Getting reservation by table");
            return reservationRequestRepository.findByTableId(tableId);

        } catch (Exception e) {
            logger.error("Failed to get reservation by table");
            throw new Exception(e.getMessage());
        }

    }

    @Override
    public ReservationRequest getReservationById(Long id) throws Exception {
        Optional<ReservationRequest> reservation = reservationRequestRepository.findById(id);
        if (reservation.isPresent()) {
            return reservation.get();
        }
        logger.error("Reservation not found");
        throw new Exception("Reservation not found");
    }

    @Override
    public List<ReservationRequest> getAllReservations() {
        return reservationRequestRepository.findAll();
    }

    @Override
    public ReservationRequest updateReservation(ReservationRequest reservation) throws Exception {
        logger.info("Updating reservation");
        Optional<ReservationRequest> reservationData = reservationRequestRepository.findById(reservation.getId());
        if (reservationData.isPresent()) {
            logger.info("Reservation found");
            ReservationRequest _reservation = reservationData.get();
            // _reservation.setRequestDate(reservation.getRequestDate());
            // _reservation.setSlotStartTime(reservation.getSlotStartTime());
            // _reservation.setSlotEndTime(reservation.getSlotEndTime());
            // _reservation.setStatus(reservation.getStatus());
            _reservation.setUpdatedAt(reservation.getUpdatedAt());
            reservationRequestRepository.save(_reservation);
        }
        logger.error("Reservation not found");
        throw new Exception("Reservation not found");
    }

    @Override
    public void cancelReservation(Long id) throws Exception {
        logger.info("Cancelling reservation");
        if (reservationRequestRepository.findById(id).isPresent()) {
            logger.info("Reservation found");
            ReservationRequest reservationRequest = reservationRequestRepository.findById(id).get();
            if (reservationRequest.getReservation().getStatus().equals("PENDING")) {
                logger.info("Reservation is pending so Cancelling it");
                reservationRequest.getReservation().setStatus("CANCELLED");
                reservationRepository.delete(reservationRequest.getReservation());
            }

            if (reservationRequest.getReservation().getStatus().equals("CONFIRMED")) {
                logger.info("Reservation is confirmed so pending with manager");
                Reservation reservation = reservationRequest.getReservation();
                reservation.setStatus("PENDINGFORCANCELLATION");
                reservationRepository.save(reservation);
            }
        } else {
            logger.error("Reservation not found");
            throw new Exception("Reservation Request not Found");
        }

    }

    public List<ReservationRequest> getAllReservationsbyRestaurant(Long restaurantId) {
        return reservationRequestRepository.findByRestaurantId(restaurantId);
    }

    public void approveReservationRequest(Long id) throws Exception {
        logger.info("Approving reservation");

        try {
            if (!reservationRequestRepository.findById(id).isPresent()) {
                logger.error("Reservation Request not found");
                throw new Exception("Reservation Request not Found");
            }
            ReservationRequest reservationRequest = reservationRequestRepository.findById(id).get();
            reservationRequest.getReservation().setStatus("CONFIRMED");
            logger.info("Reservation confirmed");

            Reservation reservation = reservationRepository.findById(reservationRequest.getReservation().getId()).get();
            reservation.setStatus("CONFIRMED");
            reservationRepository.save(reservation);
            reservationRequestRepository.save(reservationRequest);
        } catch (Exception e) {
            logger.error("Failed to approve reservation");
            throw new Exception(e.getMessage());
        }

    }

    public void rejectReservationRequest(Long id) throws Exception {
        logger.info("Rejecting reservation");
        try {
            if (!reservationRequestRepository.findById(id).isPresent()) {
                logger.error("Reservation Request not found");
                throw new Exception("Reservation Request not Found");
            }
            ReservationRequest reservationRequest = reservationRequestRepository.findById(id).get();
            reservationRequest.getReservation().setStatus("REJECTED");
            logger.info("Reservation Request rejected");
            Reservation reservation = reservationRepository.findById(reservationRequest.getReservation().getId()).get();
            reservation.setStatus("CANCELLED");
            logger.info("Reservation cancelled");
            reservationRepository.delete(reservation);
            reservationRequestRepository.save(reservationRequest);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    public void approveCancellationRequest(Long id) throws Exception {
        logger.info("Accepting cancellation");
        try {
            if (!reservationRequestRepository.findById(id).isPresent()) {
                logger.error("Reservation Request not found");
                throw new Exception("Reservation Request not Found");
            }
            ReservationRequest reservationRequest = reservationRequestRepository.findById(id).get();
            reservationRequest.getReservation().setStatus("CANCELLED");
            logger.info("Reservation cancelled");
            Reservation reservation = reservationRepository.findById(reservationRequest.getReservation().getId()).get();
            reservation.setStatus("CANCELLED");
            reservationRepository.delete(reservation);
            reservationRequest.setReservation(null);
            reservationRequestRepository.save(reservationRequest);
        } catch (Exception e) {
            logger.error("Failed to accept cancellation");
            throw new Exception(e.getMessage());

        }

    }

}
