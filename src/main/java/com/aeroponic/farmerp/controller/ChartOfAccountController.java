package com.aeroponic.farmerp.controller;

import com.aeroponic.farmerp.model.ChartOfAccount;
import com.aeroponic.farmerp.service.ChartOfAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class ChartOfAccountController {

    @Autowired
    private ChartOfAccountService chartOfAccountService;

    @GetMapping
    public List<ChartOfAccount> getAllAccounts() {
        return chartOfAccountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public ChartOfAccount getAccountById(@PathVariable Long id) {
        return chartOfAccountService.getAccountById(id);
    }

    @PostMapping
    public ResponseEntity<ChartOfAccount> createAccount(@Valid @RequestBody ChartOfAccount account) {
        return new ResponseEntity<>(chartOfAccountService.createAccount(account), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ChartOfAccount updateAccount(@PathVariable Long id, @Valid @RequestBody ChartOfAccount account) {
        return chartOfAccountService.updateAccount(id, account);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        chartOfAccountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
