package com.hashedin.reservation.services;

import com.hashedin.reservation.Config.SecurityUtil;
import com.hashedin.reservation.Dtos.RequestDtos.UserEntryDto;
import com.hashedin.reservation.entity.ReservationRequest;
import com.hashedin.reservation.entity.Restaurant;
import com.hashedin.reservation.entity.RestaurantUser;
import com.hashedin.reservation.repository.ReservationRequestRepository;
import com.hashedin.reservation.repository.RestaurantRepository;
import com.hashedin.reservation.repository.UserRepository;
import com.hashedin.reservation.services.Impl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReservationRequestRepository reservationRequestRepository;

    @Mock
    private SecurityUtil securityUtil;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateUser_UserPresent() throws Exception {
        // Arrange
        UserEntryDto user = new UserEntryDto();
        user.setEmail("test@example.com");
        user.setFullName("John Doe");
        user.setPhoneNumber("1234567890");
        user.setPassword("password");

        RestaurantUser existingUser = new RestaurantUser();
        existingUser.setEmail("test@example.com");
        existingUser.setFullName("Jane Doe");
        existingUser.setPhoneNumber("9876543210");
        existingUser.setPassword("old_password");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // Act
        userService.updateUser(user);

        // Assert
        assertEquals(user.getEmail(), existingUser.getEmail());
        assertEquals(user.getFullName(), existingUser.getFullName());
        assertEquals(user.getPhoneNumber(), existingUser.getPhoneNumber());
        assertEquals(user.getPassword(), existingUser.getPassword());

        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateUser_UserNotPresent() {
        // Arrange
        UserEntryDto user = new UserEntryDto();
        user.setEmail("test@example.com");
        user.setFullName("John Doe");
        user.setPhoneNumber("1234567890");
        user.setPassword("password");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(Exception.class, () -> userService.updateUser(user));

        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        // Arrange
        UserEntryDto userDto = new UserEntryDto();
        userDto.setEmail("test@example.com");
        userDto.setFullName("John Doe");
        userDto.setPhoneNumber("1234567890");
        userDto.setPassword("password");

        RestaurantUser user = new RestaurantUser();
        user.setEmail("test@example.com");
        user.setFullName("John Doe");
        user.setPhoneNumber("1234567890");
        user.setPassword("encoded_password");
        user.setRole("USER");
        user.setCreatedAt(LocalDate.now());
        user.setUpdatedAt(LocalDate.now());

        when(userRepository.save(user)).thenReturn(user);

        // Act
        RestaurantUser result = userService.registerUser(userDto, "USER");

        // Assert
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getFullName(), result.getFullName());
        assertEquals(user.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getRole(), result.getRole());
        assertEquals(user.getCreatedAt(), result.getCreatedAt());
        assertEquals(user.getUpdatedAt(), result.getUpdatedAt());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRegisterUser_Failure() throws Exception {
        // Arrange
        UserEntryDto userDto = new UserEntryDto();
        userDto.setEmail("test@example.com");
        userDto.setFullName("John Doe");
        userDto.setPhoneNumber("1234567890");
        userDto.setPassword("password");

        when(userRepository.save(any())).thenThrow(new RuntimeException("Registration failed"));

        // Act & Assert
        assertThrows(Exception.class, () -> userService.registerUser(userDto, "USER"));

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void testDeleteUser() {
        // Arrange
        Long userId = 1L;

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testGetUserById_UserPresent() {
        // Arrange
        Long userId = 1L;
        RestaurantUser user = new RestaurantUser();
        user.setId(userId);

        when(userRepository.getById(userId)).thenReturn(user);

        // Act
        RestaurantUser result = userService.getUserById(userId);

        // Assert
        assertEquals(user, result);

        verify(userRepository, times(1)).getById(userId);
    }

    @Test
    void testGetUserById_UserNotPresent() {
        // Arrange
        Long userId = 1L;

        when(userRepository.getById(userId)).thenReturn(null);

        // Act
        RestaurantUser result = userService.getUserById(userId);

        // Assert
        assertNull(result);

        verify(userRepository, times(1)).getById(userId);
    }

    @Test
    void testGetUserByEmail_UserPresent() {
        // Arrange
        String email = "test@example.com";
        RestaurantUser user = new RestaurantUser();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        RestaurantUser result = userService.getUserByEmail(email);

        // Assert
        assertEquals(user, result);

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testGetUserByEmail_UserNotPresent() {
        // Arrange
        String email = "test@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NullPointerException.class, () -> userService.getUserByEmail(email));

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        // TODO: Implement test case

        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> userService.getAllUsers());
    }

    @Test
    void testGetReservations() {
        // Arrange
        String currentUsername = "test@example.com";
        RestaurantUser user = new RestaurantUser();
        user.setId(1L);

        when(securityUtil.getCurrentUsername()).thenReturn(currentUsername);
        when(userRepository.findByEmail(currentUsername)).thenReturn(Optional.of(user));

        // Act
        List<ReservationRequest> result = userService.getReservations();

        // Assert
        assertNotNull(result);

        verify(securityUtil, times(1)).getCurrentUsername();
        verify(userRepository, times(1)).findByEmail(currentUsername);
        verify(reservationRequestRepository, times(1)).findByUserId(user.getId());
    }

    @Test
    void testGetRestaurantReservations() {
        // Arrange
        String currentUsername = "test@example.com";
        Long restaurantId = 1L;
        RestaurantUser user = new RestaurantUser();
        user.setId(1L);
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        when(securityUtil.getCurrentUsername()).thenReturn(currentUsername);
        when(userRepository.findByEmail(currentUsername)).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        // Act
        List<ReservationRequest> result = userService.getRestaurantReservations(restaurantId);

        // Assert
        assertNotNull(result);

        verify(securityUtil, times(1)).getCurrentUsername();
        verify(userRepository, times(1)).findByEmail(currentUsername);
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(reservationRequestRepository, times(1)).findByRestaurantId(restaurant.getId());
    }

    @Test
    void testGetManagerRestaurants_Success() throws Exception {
        // Arrange
        String currentUsername = "test@example.com";
        RestaurantUser user = new RestaurantUser();
        user.setId(1L);

        when(securityUtil.getCurrentUsername()).thenReturn(currentUsername);
        when(userRepository.findByEmail(currentUsername)).thenReturn(Optional.of(user));
        when(restaurantRepository.findByManagerId(user.getId())).thenReturn(List.of(new Restaurant()));

        // Act
        List<Restaurant> result = userService.getManagerRestaurants();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());

        verify(securityUtil, times(1)).getCurrentUsername();
        verify(userRepository, times(1)).findByEmail(currentUsername);
        verify(restaurantRepository, times(1)).findByManagerId(user.getId());
    }

    @Test
    void testGetManagerRestaurants_Failure() throws Exception {
        // Arrange
        String currentUsername = "test@example.com";

        when(securityUtil.getCurrentUsername()).thenReturn(currentUsername);
        when(userRepository.findByEmail(currentUsername)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(Exception.class, () -> userService.getManagerRestaurants());

        verify(securityUtil, times(1)).getCurrentUsername();
        verify(userRepository, times(1)).findByEmail(currentUsername);
        verifyNoMoreInteractions(restaurantRepository);
    }
}