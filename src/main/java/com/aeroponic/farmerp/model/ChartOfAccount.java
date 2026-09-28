package com.aeroponic.farmerp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "chart_of_accounts")
public class ChartOfAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Account name is required")
    @Column(length = 150, nullable = false)
    private String name;

    @NotNull(message = "Account type is required (ASSET, LIABILITY, INCOME, or EXPENSE)")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType type;

    public ChartOfAccount() {}

    public ChartOfAccount(Long id, String name, AccountType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public AccountType getType() { return type; }
    public void setType(AccountType type) { this.type = type; }
}
