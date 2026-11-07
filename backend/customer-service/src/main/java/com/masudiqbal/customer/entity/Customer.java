package com.masudiqbal.customer.entity;


import com.masudiqbal.customer.dto.CustomerRequest;
import com.masudiqbal.customer.enums.CustomerStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(
        name = "customers",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_customer_email",
                        columnNames = "email"
                )
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Customer extends BaseEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, length=100)
  private String firstName;

  @Column(nullable = false, length = 150)
  private String lastName;

  @Column(nullable = false, length = 150)
  private String email;

  @Column(length = 20)
  private String phone;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private CustomerStatus status = CustomerStatus.ACTIVE;

  public Customer(String firstName, String lastName, String email, String phone, CustomerStatus status) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.status = status;
  }

  public void deactivate() {
    this.status = CustomerStatus.INACTIVE;
  }


  public  void updateProfile(CustomerRequest request) {
    this.firstName = request.firstName();
    this.lastName = request.lastName();
    this.email = request.email();
    this.phone = request.phone();
    this.status = request.status();
  }

}