
package com.hashedin.reservation.controller;


import com.hashedin.reservation.entity.RestaurantTable;
import com.hashedin.reservation.services.Impl.RestaurantTableServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RestController
@RequestMapping("/tables")
public class RestuarantTableController {

    @Autowired
    private RestaurantTableServiceImpl tableService;

    @GetMapping
    public List<RestaurantTable> getAllTables() {
        return tableService.getAllRestaurantTables();
    }

    @GetMapping("/{id}")
    public RestaurantTable getTableById(@PathVariable Long id) {
        return tableService.getRestaurantTableById(id);
    }

    @PostMapping
    public RestaurantTable createTable(@RequestBody RestaurantTable table) {
        return tableService.createRestaurantTable(table);
    }

    @PutMapping("/{id}")
    public RestaurantTable updateTable(@PathVariable Long id, @RequestBody RestaurantTable table) throws Exception {
        return tableService.updateRestaurantTable(id, table);
    }

    @DeleteMapping("/{id}")
    public void deleteTable(@PathVariable Long id) {
       // tableRepository.delete(id);
       tableService.deleteRestaurantTable(id);
    }
}