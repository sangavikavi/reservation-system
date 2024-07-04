package com.hashedin.reservation.controller;

import com.hashedin.reservation.Dtos.RequestDtos.UserEntryDto;
import com.hashedin.reservation.entity.JwtRequest;
import com.hashedin.reservation.entity.JwtResponse;
import com.hashedin.reservation.entity.RestaurantUser;
import com.hashedin.reservation.repository.UserRepository;
import com.hashedin.reservation.security.JWTHelper;
import com.hashedin.reservation.services.Impl.UserServiceImpl;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTHelper helper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginUser_Success() {
        // Arrange
        JwtRequest request = new JwtRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        RestaurantUser user = new RestaurantUser();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole("USER");

        UserDetails userDetails = User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();

        String token = "jwt_token";

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(helper.generateToken(userDetails)).thenReturn(token);

        // Act
        ResponseEntity<?> response = userController.loginUser(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof JwtResponse);
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals(token, jwtResponse.getJwtToken());
        assertEquals(userDetails.getUsername(), jwtResponse.getUsername());

        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).matches(request.getPassword(), user.getPassword());
        verify(helper, times(1)).generateToken(userDetails);
    }

    @Test
    void testLoginUser_UnauthorizedAccess() {
        // Arrange
        JwtRequest request = new JwtRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        RestaurantUser user = new RestaurantUser();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole("ADMIN");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<?> response = userController.loginUser(request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());

        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verifyNoMoreInteractions(passwordEncoder, helper);
    }

    @Test
    void testLoginUser_UserNotFound() {
        // Arrange
        JwtRequest request = new JwtRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = userController.loginUser(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());

        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verifyNoMoreInteractions(passwordEncoder, helper);
    }

    @Test
    void testLoginUser_InvalidPassword() {
        // Arrange
        JwtRequest request = new JwtRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        RestaurantUser user = new RestaurantUser();
        user.setEmail("test@example.com");
        user.setPassword("wrong_password");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        // Act
        ResponseEntity<?> response = userController.loginUser(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());

        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).matches(request.getPassword(), user.getPassword());
        verifyNoMoreInteractions(helper);
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        // Arrange
        UserEntryDto userEntryDto = new UserEntryDto();
        userEntryDto.setEmail("test@example.com");
        userEntryDto.setPassword("password");
        userEntryDto.setFullName("John Doe");

        RestaurantUser user = new RestaurantUser();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setFullName("John Doe");

        when(userService.registerUser(userEntryDto, "USER")).thenReturn(user);

        // Act
        ResponseEntity<?> response = userController.registerUser(userEntryDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ApiResponse);
        ApiResponse apiResponse = (ApiResponse) response.getBody();
        assertEquals(user.getFullName() + " Registered Successfully", apiResponse.getDescription());

        verify(userService, times(1)).registerUser(userEntryDto, "USER");
    }

    @Test
    void testRegisterUser_Failure() throws Exception {
        // Arrange
        UserEntryDto userEntryDto = new UserEntryDto();
        userEntryDto.setEmail("test@example.com");
        userEntryDto.setPassword("password");
        userEntryDto.setFullName("John Doe");

        when(userService.registerUser(userEntryDto, "USER")).thenThrow(new RuntimeException("Registration failed"));

        // Act
        ResponseEntity<?> response = userController.registerUser(userEntryDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());

        verify(userService, times(1)).registerUser(userEntryDto, "USER");
    }

    // Add more test cases for other methods in UserController

}