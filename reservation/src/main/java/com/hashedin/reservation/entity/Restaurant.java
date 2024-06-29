package com.hashedin.reservation.entity;


import java.util.Set;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Id;
import lombok.Data;


@Data
@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String cuisines;
    private String location;
    private String workingDays;
    private String workingHours;
    private int capacity;

    @OneToMany(mappedBy= "table",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private int table_id;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Booking> bookings;



}
