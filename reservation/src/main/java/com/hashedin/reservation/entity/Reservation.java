package com.hashedin.reservation.entity;

import java.time.LocalTime;
import java.util.Date;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Data
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private RestaurantUser user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private RestaurantTable table;


    @Column(nullable = false)
    private Date reservationDate;

    @Column(nullable = false)
    private LocalTime slotStartTime;

    @Column(nullable = false)
    private LocalTime slotEndTime;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Date createdAt;

    @Column(nullable = false)
    private Date updatedAt;

    // Getters and setters
}
