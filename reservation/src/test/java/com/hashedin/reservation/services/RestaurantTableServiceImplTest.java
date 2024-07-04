package com.hashedin.reservation.services;

import com.hashedin.reservation.Dtos.RequestDtos.RestaurantTableDto;
import com.hashedin.reservation.entity.Reservation;
import com.hashedin.reservation.entity.Restaurant;
import com.hashedin.reservation.entity.RestaurantTable;
import com.hashedin.reservation.repository.ReservationRespository;
import com.hashedin.reservation.repository.RestaurantRepository;
import com.hashedin.reservation.repository.RestaurantTableRepository;
import com.hashedin.reservation.services.Impl.RestaurantTableServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantTableServiceImplTest {

    @Mock
    private RestaurantTableRepository restaurantTableRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private ReservationRespository reservationRespository;

    @Mock
    private Logger logger;

    @InjectMocks
    private RestaurantTableServiceImpl restaurantTableService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRestaurantTable_Success() throws Exception {
        // Arrange
        RestaurantTableDto restaurantTableDto = new RestaurantTableDto();
        restaurantTableDto.setCapacity(4);
        restaurantTableDto.setTableNumber(1);
        restaurantTableDto.setTableType("Regular");
        restaurantTableDto.setRestaurantId(1L);

        RestaurantTable newTable = new RestaurantTable();
        newTable.setCapacity(4);
        newTable.setTableNumber(1);
        newTable.setTableType("Regular");
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        newTable.setRestaurant(restaurant);

        when(restaurantRepository.findById(restaurantTableDto.getRestaurantId())).thenReturn(Optional.of(restaurant));
        when(restaurantTableRepository.save(any(RestaurantTable.class))).thenReturn(newTable);

        // Act
        RestaurantTable createdTable = restaurantTableService.createRestaurantTable(restaurantTableDto);

        // Assert
        assertNotNull(createdTable);
        assertEquals(4, createdTable.getCapacity());
        assertEquals(1, createdTable.getTableNumber());
        assertEquals("Regular", createdTable.getTableType());
        assertEquals(restaurant, createdTable.getRestaurant());

        verify(restaurantRepository, times(1)).findById(restaurantTableDto.getRestaurantId());
        verify(restaurantTableRepository, times(1)).save(any(RestaurantTable.class));
    }

    @Test
    void testCreateRestaurantTable_MissingRestaurantId() {
        // Arrange
        RestaurantTableDto restaurantTableDto = new RestaurantTableDto();
        restaurantTableDto.setCapacity(4);
        restaurantTableDto.setTableNumber(1);
        restaurantTableDto.setTableType("Regular");

        // Act & Assert
        assertThrows(Exception.class, () -> restaurantTableService.createRestaurantTable(restaurantTableDto));

        verify(logger, times(1)).error("Restaurant Id is required for creating table: {}", restaurantTableDto.getTableNumber());
        verifyNoMoreInteractions(restaurantRepository, restaurantTableRepository);
    }

    @Test
    void testCreateRestaurantTable_RestaurantNotFound() {
        // Arrange
        RestaurantTableDto restaurantTableDto = new RestaurantTableDto();
        restaurantTableDto.setCapacity(4);
        restaurantTableDto.setTableNumber(1);
        restaurantTableDto.setTableType("Regular");
        restaurantTableDto.setRestaurantId(1L);

        when(restaurantRepository.findById(restaurantTableDto.getRestaurantId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(Exception.class, () -> restaurantTableService.createRestaurantTable(restaurantTableDto));

        verify(logger, times(1)).error("Restaurant not found with id: {}", restaurantTableDto.getRestaurantId());
        verifyNoMoreInteractions(restaurantTableRepository);
    }

    @Test
    void testGetRestaurantTableById_ExistingId() {
        // Arrange
        Long id = 1L;
        RestaurantTable restaurantTable = new RestaurantTable();
        restaurantTable.setId(id);

        when(restaurantTableRepository.findById(id)).thenReturn(Optional.of(restaurantTable));

        // Act
        RestaurantTable retrievedTable = restaurantTableService.getRestaurantTableById(id);

        // Assert
        assertNotNull(retrievedTable);
        assertEquals(id, retrievedTable.getId());

        verify(logger, times(1)).info("Fetching restaurant table by id: {}", id);
        verify(restaurantTableRepository, times(1)).findById(id);
    }

    @Test
    void testGetRestaurantTableById_NonExistingId() {
        // Arrange
        Long id = 1L;

        when(restaurantTableRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        RestaurantTable retrievedTable = restaurantTableService.getRestaurantTableById(id);

        // Assert
        assertNull(retrievedTable);

        verify(logger, times(1)).info("Fetching restaurant table by id: {}", id);
        verify(restaurantTableRepository, times(1)).findById(id);
    }

    @Test
    void testGetAllRestaurantTables() {
        // Arrange
        List<RestaurantTable> tables = List.of(new RestaurantTable(), new RestaurantTable());

        when(restaurantTableRepository.findAll()).thenReturn(tables);

        // Act
        List<RestaurantTable> retrievedTables = restaurantTableService.getAllRestaurantTables();

        // Assert
        assertNotNull(retrievedTables);
        assertEquals(2, retrievedTables.size());

        verify(logger, times(1)).info("Fetching all restaurant tables");
        verify(restaurantTableRepository, times(1)).findAll();
    }

    @Test
    void testUpdateRestaurantTable_Success() throws Exception {
        // Arrange
        Long id = 1L;
        RestaurantTable updatedTable = new RestaurantTable();
        updatedTable.setCapacity(4);
        updatedTable.setTableNumber(1);

        RestaurantTable existingTable = new RestaurantTable();
        existingTable.setId(id);

        when(reservationRespository.findByTableId(id)).thenReturn(List.of());
        when(restaurantTableRepository.findById(id)).thenReturn(Optional.of(existingTable));
        when(restaurantTableRepository.save(any(RestaurantTable.class))).thenReturn(updatedTable);

        // Act
        RestaurantTable updated = restaurantTableService.updateRestaurantTable(id, updatedTable);

        // Assert
        assertNotNull(updated);
        assertEquals(4, updated.getCapacity());
        assertEquals(1, updated.getTableNumber());

        verify(logger, times(1)).info("Updating restaurant table: {}", id);
        verify(reservationRespository, times(1)).findByTableId(id);
        verify(restaurantTableRepository, times(1)).findById(id);
        verify(restaurantTableRepository, times(1)).save(any(RestaurantTable.class));
    }

    @Test
    void testUpdateRestaurantTable_WithBookings() {
        // Arrange
        Long id = 1L;
        RestaurantTable updatedTable = new RestaurantTable();
        updatedTable.setCapacity(4);
        updatedTable.setTableNumber(1);
    
        when(reservationRespository.findByTableId(id)).thenReturn(List.of(new Reservation()));
    
        // Act & Assert
        assertThrows(Exception.class, () -> restaurantTableService.updateRestaurantTable(id, updatedTable));
    
        verify(logger, times(1)).info("Updating restaurant table: {}", id);
        verify(logger, times(1)).error("Table has bookings, cannot update: {}", id);
        verifyNoMoreInteractions(restaurantTableRepository);
    }

    @Test
    void testUpdateRestaurantTable_TableNotFound() {
        // Arrange
        Long id = 1L;
        RestaurantTable updatedTable = new RestaurantTable();
        updatedTable.setCapacity(4);
        updatedTable.setTableNumber(1);

        when(reservationRespository.findByTableId(id)).thenReturn(List.of());
        when(restaurantTableRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(Exception.class, () -> restaurantTableService.updateRestaurantTable(id, updatedTable));

        verify(logger, times(1)).info("Updating restaurant table: {}", id);
        verify(logger, times(1)).error("Restaurant table not found: {}", id);
        verifyNoMoreInteractions(restaurantTableRepository);
    }

    @Test
    void testDeleteRestaurantTable_Success() throws Exception {
        // Arrange
        Long id = 1L;

        when(reservationRespository.findByTableId(id)).thenReturn(List.of());

        // Act
        assertDoesNotThrow(() -> restaurantTableService.deleteRestaurantTable(id));

        verify(logger, times(1)).info("Deleting restaurant table: {}", id);
        verify(reservationRespository, times(1)).findByTableId(id);
        verify(restaurantTableRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteRestaurantTable_WithBookings() {
        // Arrange
        Long id = 1L;

        when(reservationRespository.findByTableId(id)).thenReturn(List.of(new Reservation()));

        // Act & Assert
        assertThrows(Exception.class, () -> restaurantTableService.deleteRestaurantTable(id));

        verify(logger, times(1)).info("Deleting restaurant table: {}", id);
        verify(logger, times(1)).error("Table has bookings, cannot delete: {}", id);
        verifyNoMoreInteractions(restaurantTableRepository);
    }

    @Test
    void testGetTablesByRestaurantId() {
        // Arrange
        Long id = 1L;
        List<RestaurantTable> tables = List.of(new RestaurantTable(), new RestaurantTable());

        when(restaurantTableRepository.findByRestaurantId(id)).thenReturn(tables);

        // Act
        List<RestaurantTable> retrievedTables = restaurantTableService.getTablesByRestaurantId(id);

        // Assert
        assertNotNull(retrievedTables);
        assertEquals(2, retrievedTables.size());

        verify(logger, times(1)).info("Fetching tables for restaurant id: {}", id);
        verify(restaurantTableRepository, times(1)).findByRestaurantId(id);
    }
}