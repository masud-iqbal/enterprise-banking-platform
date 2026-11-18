package com.masudiqbal.customer.controller;

import com.masudiqbal.customer.dto.CustomerRequest;
import com.masudiqbal.customer.dto.CustomerResponse;
import com.masudiqbal.customer.dto.PageResponse;
import com.masudiqbal.customer.exception.ApiErrorResponse;
import com.masudiqbal.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@AllArgsConstructor
@Tag(
        name="Customer Management",
        description = "APIs for managing banking customers"
)
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    @Operation(
            summary = "Create a customer",
            description = "Registers a new banking customer"
    )
    @ApiResponses({
            @ApiResponse(
                responseCode = "201",
                    description = "Customer created successfully",
                    content = @Content(
                            schema = @Schema(implementation = CustomerResponse.class)
                    )
            ),
            @ApiResponse(
                responseCode = "400",
                    description = "Invalid customer request",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Customer email already exists",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                    schema = @Schema(implementation = ApiErrorResponse.class)
            )
    ),
    })
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid @RequestBody CustomerRequest request) {
        CustomerResponse response =  customerService.createCustomer(request);
        return  ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get customer by ID",
            description = "Retrieves a customer using their unique identifier"
    )
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable UUID id) {
       CustomerResponse response = customerService.getCustomerById(id);
       return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(
            summary = "Get All Customer",
            description = "Retrieves all customer with pagination"
    )
    public ResponseEntity<PageResponse<CustomerResponse>> getAllCustomers (
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
         PageResponse<CustomerResponse> response = customerService.getAllCustomer(page, size);
         return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update customer",
            description = "Update a particular customer"
    )
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable UUID id,
                                                           @Valid @RequestBody CustomerRequest request) {
        CustomerResponse response =  customerService.updateCustomer(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(
            summary = "Deactivate customer",
            description = "Deactivate a particular customer"
    )
    public ResponseEntity<CustomerResponse> deactivateCustomer(@PathVariable UUID id) {
           CustomerResponse response =  customerService.deactivateCustomer(id);
           return ResponseEntity.ok(response);
    }

}
