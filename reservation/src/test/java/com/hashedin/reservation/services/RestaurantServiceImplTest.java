package com.hashedin.reservation.services;

import com.hashedin.reservation.Config.SecurityUtil;
import com.hashedin.reservation.Dtos.RequestDtos.RestaurantDto;
import com.hashedin.reservation.Dtos.ResponseDto.RestaurantResponseDto;
import com.hashedin.reservation.entity.Restaurant;
import com.hashedin.reservation.entity.RestaurantTable;
import com.hashedin.reservation.entity.RestaurantUser;
import com.hashedin.reservation.repository.RestaurantRepository;
import com.hashedin.reservation.services.Impl.RestaurantServiceImpl;
import com.hashedin.reservation.services.Impl.RestaurantTableServiceImpl;
import com.hashedin.reservation.services.Impl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantTableServiceImpl restaurantTableService;

    @Mock
    private SecurityUtil securityUtil;

    @Mock
    private UserServiceImpl uService;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    private static final Logger logger = LoggerFactory.getLogger(RestaurantServiceImpl.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRestaurant_Success() throws Exception {
        // Arrange
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setName("Test Restaurant");
        restaurantDto.setLocation("Test Location");
        restaurantDto.setCuisines("Test Cuisines");
        List<String> workingDays = new ArrayList<>();
        workingDays.add("Test Working Days");
        restaurantDto.setWorkingDays(workingDays);
        restaurantDto.setOpeningTime(LocalTime.parse("10:00:00"));
        restaurantDto.setClosingTime(LocalTime.parse("11:00:00"));

        RestaurantUser manager = new RestaurantUser();
        manager.setEmail("test@example.com");

        when(restaurantRepository.findByname(restaurantDto.getName())).thenReturn(null);
        when(uService.getUserByEmail(securityUtil.getCurrentUsername())).thenReturn(manager);
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurantDto);

        // Assert
        assertNotNull(createdRestaurant);
        assertEquals(restaurantDto.getName(), createdRestaurant.getName());
        assertEquals(restaurantDto.getLocation(), createdRestaurant.getLocation());
        assertEquals(restaurantDto.getCuisines(), createdRestaurant.getCuisines());
        assertEquals(manager, createdRestaurant.getManager());

        verify(restaurantRepository, times(1)).findByname(restaurantDto.getName());
        verify(uService, times(1)).getUserByEmail(securityUtil.getCurrentUsername());
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    void testCreateRestaurant_RestaurantAlreadyExists() throws Exception {
        // Arrange
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setName("Test Restaurant");

        when(restaurantRepository.findByname(restaurantDto.getName())).thenReturn(new Restaurant());

        // Act & Assert
        assertThrows(Exception.class, () -> restaurantService.createRestaurant(restaurantDto));

        verify(restaurantRepository, times(1)).findByname(restaurantDto.getName());
        verifyNoMoreInteractions(uService, restaurantRepository);
    }

    @Test
    void testGetAllRestaurants() {
        // Arrange
        int page = 0;
        int size = 10;
        PageRequest pageable = PageRequest.of(page, size);
        Page<Restaurant> expectedPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(restaurantRepository.findAll(pageable)).thenReturn(expectedPage);

        // Act
        Page<Restaurant> result = restaurantService.getAllRestaurants(page, size);

        // Assert
        assertNotNull(result);
        assertEquals(expectedPage, result);

        verify(restaurantRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetRestaurantById_Success() throws Exception {
        // Arrange
        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        List<RestaurantTable> tables = new ArrayList<>();

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurantTableService.getTablesByRestaurantId(restaurantId)).thenReturn(tables);

        // Act
        RestaurantResponseDto response = restaurantService.getRestaurantById(restaurantId);

        // Assert
        assertNotNull(response);
        assertEquals(restaurant, response.getRestaurant());
        assertEquals(tables, response.getTables());

        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(restaurantTableService, times(1)).getTablesByRestaurantId(restaurantId);
    }

    @Test
    void testGetRestaurantById_RestaurantNotFound() {
        // Arrange
        Long restaurantId = 1L;

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(Exception.class, () -> restaurantService.getRestaurantById(restaurantId));

        verify(restaurantRepository, times(1)).findById(restaurantId);
        verifyNoMoreInteractions(restaurantTableService);
    }

    @Test
    void testUpdateRestaurant_Success() {
        // Arrange
        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setName("Old Name");

        Restaurant updatedRestaurant = new Restaurant();
        updatedRestaurant.setId(restaurantId);
        updatedRestaurant.setName("New Name");

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Restaurant result = restaurantService.updateRestaurant(restaurantId, updatedRestaurant);

        // Assert
        assertNotNull(result);
        assertEquals(updatedRestaurant.getName(), result.getName());

        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    void testUpdateRestaurant_RestaurantNotFound() {
        // Arrange
        Long restaurantId = 1L;
        Restaurant updatedRestaurant = new Restaurant();

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // Act
        Restaurant result = restaurantService.updateRestaurant(restaurantId, updatedRestaurant);

        // Assert
        assertNull(result);

        verify(restaurantRepository, times(1)).findById(restaurantId);
        verifyNoMoreInteractions(restaurantRepository);
    }

    @Test
    void testDeleteRestaurant() {
        // Arrange
        Long restaurantId = 1L;

        // Act
        restaurantService.deleteRestaurant(restaurantId);

        // Assert
        verify(restaurantRepository, times(1)).deleteById(restaurantId);
    }
}
