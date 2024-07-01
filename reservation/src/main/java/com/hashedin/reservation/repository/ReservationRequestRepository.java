package com.hashedin.reservation.repository;
import com.hashedin.reservation.entity.ReservationRequest;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRequestRepository extends JpaRepository<ReservationRequest, Long>{

    Optional<ReservationRequest> findById(Long id);

    ReservationRequest findByTableId(Long tableId);

    List<ReservationRequest> findByUserId(Long userId);

    List<ReservationRequest> findByRestaurantId(Long restaurantId);
	
}
