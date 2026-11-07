package com.masudiqbal.customer.dto;

import com.masudiqbal.customer.entity.Customer;
import com.masudiqbal.customer.enums.CustomerStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerRequest(
        @NotBlank(message = "First name is required")
        @Size(max = 100, message = "First name cannot exceed 100 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 100, message = "Last name cannot exceed 100 characters")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        @Size(max = 150, message = "Email cannot exceed 150 characters")
        String email,

        @Size(max = 20, message = "Phone cannot exceed 20 characters")
        String phone,

        @NotNull(message = "Status is required")
        CustomerStatus status
) {
}
