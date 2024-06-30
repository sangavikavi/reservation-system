
package com.hashedin.reservation.services;

import java.util.List;

import com.hashedin.reservation.entity.RestaurantTable;


public interface RestaurantTableService {
    
    RestaurantTable createRestaurantTable(RestaurantTable restaurantTable);
    
    RestaurantTable getRestaurantTableById(Long id);
    
    List<RestaurantTable> getAllRestaurantTables();
    
    RestaurantTable updateRestaurantTable(Long id, RestaurantTable updatedTable) throws Exception;
    
    void deleteRestaurantTable(Long id);
}
