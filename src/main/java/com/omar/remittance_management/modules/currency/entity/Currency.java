package com.omar.remittance_management.modules.currency.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "currencies")
@Setter
@Getter
@NoArgsConstructor

public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 3)
    private String code;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "symbol", length = 5)
    private String symbol;

    @Column(name = "active")
    private boolean active = true;

    public Currency(String code, String name, String symbol, boolean active) {
        this.code = code;
        this.name = name;
        this.symbol = symbol;

        this.active = active;
    }

}
