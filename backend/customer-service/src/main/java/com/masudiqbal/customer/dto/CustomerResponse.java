package com.masudiqbal.customer.dto;

import com.masudiqbal.customer.enums.CustomerStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        LocalDateTime createdAt,
        CustomerStatus status
) {
}
