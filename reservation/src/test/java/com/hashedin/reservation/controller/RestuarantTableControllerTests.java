package com.hashedin.reservation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.hashedin.reservation.services.Impl.RestaurantTableServiceImpl;

import static org.mockito.Mockito.when;

@WebMvcTest(RestuarantTableController.class)
public class RestuarantTableControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantTableServiceImpl tableService;

    @Test
    public void deleteTable_shouldReturnOk() throws Exception {
        Long tableId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/tables/{id}", tableId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Table deleted successfully"));

        // Add additional assertions or verifications if needed
    }

}