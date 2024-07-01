
package com.hashedin.reservation.controller;

import com.hashedin.reservation.Dtos.RequestDtos.RestaurantTableDto;
import com.hashedin.reservation.entity.RestaurantTable;
import com.hashedin.reservation.services.Impl.RestaurantTableServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    // @GetMapping("/{id }")
    // public RestaurantTable getTableById(@PathVariable Long id) {
    // return tableService.getRestaurantTableById(id);
    // }

    @PostMapping("/add")
    public ResponseEntity<?> createTable(@RequestBody RestaurantTableDto table) {
        try {
            tableService.createRestaurantTable(table);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Table created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTable(@PathVariable Long id, @RequestBody RestaurantTable table) {

        try {
            return ResponseEntity.ok(tableService.updateRestaurantTable(id, table));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTable(@PathVariable Long id) {
        // tableRepository.delete(id);
        try {
            tableService.deleteRestaurantTable(id);
            return ResponseEntity.ok("Table deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}