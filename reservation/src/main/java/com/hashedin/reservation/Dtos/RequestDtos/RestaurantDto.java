package com.hashedin.reservation.Dtos.RequestDtos;

import com.hashedin.reservation.entity.RestaurantUser;

import lombok.Data;

@Data
public class RestaurantDto {
    private String name;
    private String cuisines;
    private String location;
}

