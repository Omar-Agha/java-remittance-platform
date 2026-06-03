package com.omar.remittance_management.modules.customer.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;

    public String getFullName() {
        return (firstName != null ? firstName : "") +
                (firstName != null && lastName != null ? " " : "") +
                (lastName != null ? lastName : "");
    }
}