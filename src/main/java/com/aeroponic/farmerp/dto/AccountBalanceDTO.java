package com.aeroponic.farmerp.dto;

import com.aeroponic.farmerp.model.AccountType;
import java.math.BigDecimal;

public class AccountBalanceDTO {
    private Long id;
    private String name;
    private AccountType type;
    private BigDecimal balance;

    public AccountBalanceDTO() {}

    public AccountBalanceDTO(Long id, String name, AccountType type, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.balance = balance;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public AccountType getType() { return type; }
    public void setType(AccountType type) { this.type = type; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}
