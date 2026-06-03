package com.omar.remittance_management.modules.customer.entity.address;

import com.omar.remittance_management.modules.customer.entity.Customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@Table(name = "customer_address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String address1;

    @Column(nullable = true)
    private String address2;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    @jakarta.validation.constraints.Min(0)
    private int sort_order;

    @jakarta.persistence.ManyToOne(optional = false, fetch = FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Shadow property for the foreign key customerId
    @Column(name = "customer_id", insertable = false, updatable = false)
    private Long customerId;

}
