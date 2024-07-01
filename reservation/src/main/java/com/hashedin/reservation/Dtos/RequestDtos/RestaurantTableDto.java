package com.hashedin.reservation.Dtos.RequestDtos;

import lombok.Data;

@Data
public class RestaurantTableDto {
    private String tableType;

    private Long restaurantId;

    private int capacity;

    private int tableNumber;
    
}
