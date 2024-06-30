package com.hashedin.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hashedin.reservation.entity.RestaurantTable;
import java.util.List;



@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
   RestaurantTable findByTableId(Long tableId);
    List<RestaurantTable> getAllTables();
    RestaurantTable getTableById(Long id);
    
}

