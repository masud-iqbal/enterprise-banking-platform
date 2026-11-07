package com.masudiqbal.customer.service;

import com.masudiqbal.customer.dto.CustomerRequest;
import com.masudiqbal.customer.dto.CustomerResponse;
import com.masudiqbal.customer.entity.Customer;
import com.masudiqbal.customer.enums.CustomerStatus;
import com.masudiqbal.customer.exception.DuplicateCustomerException;
import com.masudiqbal.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void shouldCreateCustomerSuccessfully() {
        CustomerRequest request =  new CustomerRequest("Masud", "Ikbal", "masud.cseu@gmail.com", "+8801712243940", CustomerStatus.ACTIVE);

        when(customerRepository.existsByEmail("masud.cseu@gmail.com"))
                .thenReturn(false);
        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act: Execute the business operation.
        CustomerResponse response =  customerService.createCustomer(request);

        //Assert: Verify the result
        assertNotNull(response);

        assertEquals("Masud", response.firstName());
        assertEquals("Ikbal", response.lastName());
        assertEquals("masud.cseu@gmail.com", response.email());
        assertEquals("ACTIVE", response.status().name());

        //Verify that the repository was called
        verify(customerRepository).existsByEmail("masud.cseu@gmail.com");
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void sholdRejectDuplicateCustomerEmail() {
        CustomerRequest request = new CustomerRequest("Masud", "Iqbal", "masud@gmail.com", "+8801712243940", CustomerStatus.ACTIVE);
        when(customerRepository.existsByEmail("masud@gmail.com")).thenReturn(true);

        // Act + Assert
        assertThrows(
                DuplicateCustomerException.class,
                () -> customerService.createCustomer(request)
        );
        //No database save should happen.
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void shouldNormalizeEmailBeforeSaving() {

        // Arrange
        CustomerRequest request =
                new CustomerRequest(
                        "Masud",
                        "Ikbal",
                        " MASUD@EXAMPLE.COM ",
                        "+8801712345678",
                        CustomerStatus.INACTIVE
                );

        when(customerRepository.existsByEmail("masud@example.com"))
                .thenReturn(false);

        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        customerService.createCustomer(request);

        // Assert
        ArgumentCaptor<Customer> captor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerRepository).save(captor.capture());

        Customer savedCustomer = captor.getValue();

        assertEquals("masud@example.com", savedCustomer.getEmail());
    }

}
