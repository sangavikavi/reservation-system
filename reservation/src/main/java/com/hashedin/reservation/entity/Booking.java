package com.hashedin.reservation.entity;


import java.time.LocalDate;
import java.time.LocalTime;

import com.hashedin.reservation.enums.BookingStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.Id;

@Data
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany
    @JoinColumn(name = "table_id", nullable = false)
    private ResturantTable table;

    private LocalDate date;
    private LocalTime timeSlot;
    private int numberOfPeople;
    private BookingStatus status;

    // getters and setters
}
