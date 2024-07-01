package com.hashedin.reservation.entity;




import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
@Table(name = "reservation_requests")
public class ReservationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    // @Column(nullable = false)
    // private Date requestDate;

    // @Column(nullable = false)
    // private Time slotStartTime;

    // @Column(nullable = false)
    // private Time slotEndTime;

    // @Column(nullable = false)
    // private String status;

    @Column(nullable = false)
    private Long tableId;

    @Column(nullable = false)
    private Long restaurantId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Date createdAt;

    @Column(nullable = false)
    private Date updatedAt;
    
}