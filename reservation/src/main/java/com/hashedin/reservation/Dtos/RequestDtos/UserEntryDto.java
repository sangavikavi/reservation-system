package com.hashedin.reservation.Dtos.RequestDtos;
import java.time.LocalDate;

import lombok.Data;

@Data
public class UserEntryDto {

    private String fullName;

    private String email;

    private String phoneNumber;

    private String password;
}
