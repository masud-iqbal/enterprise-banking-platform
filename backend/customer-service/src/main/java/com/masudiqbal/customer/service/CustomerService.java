package com.masudiqbal.customer.service;

import com.masudiqbal.customer.dto.CustomerRequest;
import com.masudiqbal.customer.dto.CustomerResponse;
import com.masudiqbal.customer.dto.PageResponse;
import com.masudiqbal.customer.entity.Customer;
import com.masudiqbal.customer.exception.CustomerNotFoundException;
import com.masudiqbal.customer.exception.DuplicateCustomerException;
import com.masudiqbal.customer.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        String normalizeEmail = request.email().trim().toLowerCase(Locale.ROOT);
        //Business rule: email must be unique.
        if(customerRepository.existsByEmail(normalizeEmail)) {
            throw new DuplicateCustomerException("A customer with this email already exists");
        }

        //Convert request DTO into a JPA entity
        Customer customer = new Customer(
                request.firstName().trim(),
                request.lastName().trim(),
                normalizeEmail,
                request.phone(),
                request.status()
        );

        //Persist customer in the DB
        Customer savedCustomer = customerRepository.save(customer);

        //Convert saved entity into response DTO
        return new CustomerResponse(
            savedCustomer.getId(),
                savedCustomer.getFirstName(),
                savedCustomer.getLastName(),
                savedCustomer.getEmail(),
                savedCustomer.getPhone(),
                savedCustomer.getCreatedAt(),
                savedCustomer.getStatus()
        );
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: "+ id));
        return toCustomerResponse(customer);
    }

    @Transactional(readOnly = true)
    public PageResponse<CustomerResponse> getAllCustomer(int page, int size) {
        if(page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }
        if(size <1 || size > 100) {
            throw new IllegalArgumentException("Page size must be between 1 and 100");
        }
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Customer> customerPage = customerRepository.findAll(pageable);
        List<CustomerResponse> content =    customerPage
                .getContent()
                .stream()
                .map(this::toCustomerResponse)
                .toList();
        return new PageResponse<>(
                content,
                customerPage.getNumber(),
                customerPage.getSize(),
                customerPage.getTotalElements(),
                customerPage.getTotalPages(),
                customerPage.isFirst(),
                customerPage.isLast()
        );

    }

    private CustomerResponse toCustomerResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getCreatedAt(),
                customer.getStatus()
        );
    }

    @Transactional
    public CustomerResponse updateCustomer(UUID id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(
                        "Customer not found with ID: "+ id
                ));

        String normalizeEmail = request.email().trim().toLowerCase(Locale.ROOT);
        if(customerRepository.existsByEmailAndIdNot(normalizeEmail, id)) {
            throw new DuplicateCustomerException("Another customer already uses this email");
        }

        //Persist customer in the DB
        customer.updateProfile(request);
        Customer savedCustomer = customerRepository.save(customer);

        //Convert saved entity into response DTO
        return toCustomerResponse(customer);
    }

    @Transactional
    public CustomerResponse deactivateCustomer(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(
                   "Customer not found with ID: "+ id
                ));

        customer.deactivate();
        Customer savedCustomer = customerRepository.save(customer);
        return toCustomerResponse(savedCustomer);
    }


}
