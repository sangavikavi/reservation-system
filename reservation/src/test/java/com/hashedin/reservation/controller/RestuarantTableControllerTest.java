package com.hashedin.reservation.controller;

import com.hashedin.reservation.Dtos.RequestDtos.RestaurantTableDto;
import com.hashedin.reservation.entity.RestaurantTable;
import com.hashedin.reservation.services.Impl.RestaurantTableServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RestuarantTableControllerTest {

    @Mock
    private RestaurantTableServiceImpl tableService;

    @InjectMocks
    private RestuarantTableController tableController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTables() {
        // Arrange
        List<RestaurantTable> expectedTables = new ArrayList<>();
        when(tableService.getAllRestaurantTables()).thenReturn(expectedTables);

        // Act
        List<RestaurantTable> actualTables = tableController.getAllTables();

        // Assert
        assertEquals(expectedTables, actualTables);
        verify(tableService, times(1)).getAllRestaurantTables();
    }

    @Test
    void testCreateTable_Success() throws Exception {
        // Arrange
        RestaurantTableDto tableDto = new RestaurantTableDto();
        doNothing().when(tableService).createRestaurantTable(tableDto);

        // Act
        ResponseEntity<?> response = tableController.createTable(tableDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Table created successfully", response.getBody());
        verify(tableService, times(1)).createRestaurantTable(tableDto);
    }

    @Test
    void testCreateTable_Failure() throws Exception {
        // Arrange
        RestaurantTableDto tableDto = new RestaurantTableDto();
        String errorMessage = "Error creating table";
        doThrow(new RuntimeException(errorMessage)).when(tableService).createRestaurantTable(tableDto);

        // Act
        ResponseEntity<?> response = tableController.createTable(tableDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(tableService, times(1)).createRestaurantTable(tableDto);
    }

    @Test
    void testUpdateTable_Success() throws Exception {
        // Arrange
        Long id = 1L;
        RestaurantTable table = new RestaurantTable();
        when(tableService.updateRestaurantTable(id, table)).thenReturn(table);

        // Act
        ResponseEntity<?> response = tableController.updateTable(id, table);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(table, response.getBody());
        verify(tableService, times(1)).updateRestaurantTable(id, table);
    }

    @Test
    void testUpdateTable_Failure() throws Exception {
        // Arrange
        Long id = 1L;
        RestaurantTable table = new RestaurantTable();
        String errorMessage = "Error updating table";
        doThrow(new RuntimeException(errorMessage)).when(tableService).updateRestaurantTable(id, table);

        // Act
        ResponseEntity<?> response = tableController.updateTable(id, table);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(tableService, times(1)).updateRestaurantTable(id, table);
    }

    @Test
    void testDeleteTable_Success() throws Exception {
        // Arrange
        Long id = 1L;
        doNothing().when(tableService).deleteRestaurantTable(id);

        // Act
        ResponseEntity<?> response = tableController.deleteTable(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Table deleted successfully", response.getBody());
        verify(tableService, times(1)).deleteRestaurantTable(id);
    }

    @Test
    void testDeleteTable_Failure() throws Exception {
        // Arrange
        Long id = 1L;
        String errorMessage = "Error deleting table";
        doThrow(new RuntimeException(errorMessage)).when(tableService).deleteRestaurantTable(id);

        // Act
        ResponseEntity<?> response = tableController.deleteTable(id);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(tableService, times(1)).deleteRestaurantTable(id);
    }
}