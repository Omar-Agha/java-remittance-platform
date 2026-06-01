package com.omar.remittance_management.modules.customer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;



import jakarta.persistence.Column;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "national_number", unique = true, nullable = false)
    private String nationalNumber;
    private String firstName;

    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    private String phoneNumber;
    private java.time.LocalDate birthDate;
    private String address;
    @Column(name = "nationality", nullable = true)
    private String nationality;
    private Boolean isActive;

    public String getFullName() {
        return (firstName != null ? firstName : "") +
               (firstName != null && lastName != null ? " " : "") +
               (lastName != null ? lastName : "");
    }
}