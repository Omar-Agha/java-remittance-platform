package com.omar.remittance_management.modules.customer.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;


public record  CreateCustomer (
    @NotBlank(message = "National number is required")
    @Size(max = 255, message = "National number must be at most 255 characters")
    String nationalNumber,

    @NotBlank(message = "First name is required")
    @Size(max = 255, message = "First name must be at most 255 characters")
    String firstName,

    @NotBlank(message = "Last name is required")
    @Size(max = 255, message = "Last name must be at most 255 characters")
     String lastName,

    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email must be at most 255 characters")
     String email,

    @Size(max = 255, message = "Phone number must be at most 255 characters")
     String phoneNumber,

    @Past(message = "Birth date must be in the past")
    @NotNull(message = "Birth date is required")
     LocalDate birthDate,

    @Size(max = 255, message = "Address must be at most 255 characters")
     String address,

    @Size(max = 255, message = "Nationality must be at most 255 characters")
     String nationality,

    @NotNull(message = "isActive is required")
     Boolean isActive
){}

