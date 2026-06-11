package com.omar.remittance_management.modules.tax.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "taxes")
@Getter
@Setter
public class Tax {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    @jakarta.validation.constraints.DecimalMin(value = "0.00", inclusive = true)
    @jakarta.validation.constraints.DecimalMax(value = "1.00", inclusive = true)
    private BigDecimal rate;

    @Column(nullable = false)
    private Boolean active;

    // Constructors
    public Tax() {
    }

    public Tax(String name, BigDecimal rate, Boolean active) {
        this.name = name;
        this.rate = rate;
        this.active = active;
    }

}
