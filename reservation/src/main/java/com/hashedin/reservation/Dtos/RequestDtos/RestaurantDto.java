package com.hashedin.reservation.Dtos.RequestDtos;

import java.time.LocalTime;
import java.util.List;

import lombok.Data;

@Data
public class RestaurantDto {
    private String name;
    private String cuisines;
    private String location;
    private List<String> workingDays;
    private LocalTime openingTime;
    private LocalTime closingTime;
}

