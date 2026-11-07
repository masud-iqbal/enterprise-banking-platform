package com.masudiqbal.customer.controller;


import com.masudiqbal.customer.dto.CustomerRequest;
import com.masudiqbal.customer.dto.CustomerResponse;
import com.masudiqbal.customer.enums.CustomerStatus;
import com.masudiqbal.customer.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Test
    void shouldCreateCustomerAndReturn202() throws Exception {
        UUID customerId = UUID.randomUUID();
        CustomerResponse response = new CustomerResponse(
          customerId,
          "Masud",
          "Ikbal",
          "masud@gmail.com",
          "+8801712243940",
                LocalDateTime.now(),
                CustomerStatus.ACTIVE
        );

        when(customerService.createCustomer(any(CustomerRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "firstName": "Masud",
                           "lastName": "Ikbal",
                           "email": "masud@gmail.com",
                           "phone": "+8801712243940",
                           "status": "ACTIVE"           
                        }
                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(customerId.toString()))
                .andExpect(jsonPath("$.firstName").value("Masud"))
                .andExpect(jsonPath("$.email").value("masud@gmail.com"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldReturn400WhenRequestValidationFails() throws Exception {

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "",
                                  "lastName": "Ikbal",
                                  "email": "invalid-email",
                                  "phone": "+8801712345678",
                                  "status": "ACTIVE"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validateErrors.firstName").exists())
                .andExpect(jsonPath("$.validateErrors.email").exists());
    }

    @Test
    void shouldReturnCustomerById() throws Exception {

        UUID customerId = UUID.randomUUID();

        CustomerResponse response = new CustomerResponse(
                customerId,
                "Masud",
                "Ikbal",
                "masud@gmail.com",
                "+8801712345678",
                LocalDateTime.now(),
                CustomerStatus.ACTIVE
        );

        when(customerService.getCustomerById(customerId))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/customers/{id}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId.toString()))
                .andExpect(jsonPath("$.firstName").value("Masud"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

}
