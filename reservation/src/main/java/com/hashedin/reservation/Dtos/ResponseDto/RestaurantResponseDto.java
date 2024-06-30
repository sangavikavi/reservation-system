package com.hashedin.reservation.Dtos.ResponseDto;

import java.util.List;

import com.hashedin.reservation.entity.Restaurant;
import com.hashedin.reservation.entity.RestaurantTable;

import lombok.Data;


@Data
public class RestaurantResponseDto {
    
    private Restaurant restaurant;
    private List<RestaurantTable> tables;
}
