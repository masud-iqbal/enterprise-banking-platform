package com.masudiqbal.customer.controller;

import com.masudiqbal.customer.dto.CustomerRequest;
import com.masudiqbal.customer.dto.CustomerResponse;
import com.masudiqbal.customer.dto.PageResponse;
import com.masudiqbal.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid @RequestBody CustomerRequest request) {
        CustomerResponse response =  customerService.createCustomer(request);
        return  ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable UUID id) {
       CustomerResponse response = customerService.getCustomerById(id);
       return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<CustomerResponse>> getAllCustomers (
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
         PageResponse<CustomerResponse> response = customerService.getAllCustomer(page, size);
         return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable UUID id,
                                                           @Valid @RequestBody CustomerRequest request) {
        CustomerResponse response =  customerService.updateCustomer(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CustomerResponse> deactivateCustomer(@PathVariable UUID id) {
           CustomerResponse response =  customerService.deactivateCustomer(id);
           return ResponseEntity.ok(response);
    }

}
