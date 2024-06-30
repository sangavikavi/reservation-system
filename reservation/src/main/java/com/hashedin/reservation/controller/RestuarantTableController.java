
package com.hashedin.reservation.controller;


import com.hashedin.reservation.entity.RestaurantTable;
import com.hashedin.reservation.repository.RestaurantTableRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RestController
@RequestMapping("/tables")
public class RestuarantTableController {

    @Autowired
    private RestaurantTableRepository tableRepository;

    @GetMapping
    public List<RestaurantTable> getAllTables() {
        return tableRepository.getAllTables();
    }

    @GetMapping("/{id}")
    public RestaurantTable getTableById(@PathVariable Long id) {
        return tableRepository.getTableById(id);
    }

    @PostMapping
    public RestaurantTable createTable(@RequestBody RestaurantTable table) {
        return tableRepository.save(table);
    }

    @PutMapping("/{id}")
    public RestaurantTable updateTable(@PathVariable Long id, @RequestBody RestaurantTable table) {
        return tableRepository.save(table);
    }

    @DeleteMapping("/{id}")
    public void deleteTable(@PathVariable Long id) {
       // tableRepository.delete(id);
    }
}