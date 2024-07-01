
package com.hashedin.reservation.services;

import java.util.List;

import com.hashedin.reservation.Dtos.RequestDtos.RestaurantTableDto;
import com.hashedin.reservation.entity.RestaurantTable;


public interface RestaurantTableService {
    
    RestaurantTable createRestaurantTable(RestaurantTableDto restaurantTable) throws Exception;
    
    RestaurantTable getRestaurantTableById(Long id);
    
    List<RestaurantTable> getAllRestaurantTables();
    
    RestaurantTable updateRestaurantTable(Long id, RestaurantTable updatedTable) throws Exception;
    
    void deleteRestaurantTable(Long id) throws Exception;
}
