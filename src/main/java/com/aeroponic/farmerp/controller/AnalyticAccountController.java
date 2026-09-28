package com.aeroponic.farmerp.controller;

import com.aeroponic.farmerp.model.AnalyticAccount;
import com.aeroponic.farmerp.service.AnalyticAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytic-accounts")
public class AnalyticAccountController {

    @Autowired
    private AnalyticAccountService analyticAccountService;

    @GetMapping
    public List<AnalyticAccount> getAllAnalyticAccounts() {
        return analyticAccountService.getAllAnalyticAccounts();
    }

    @PostMapping
    public ResponseEntity<AnalyticAccount> createAnalyticAccount(@Valid @RequestBody AnalyticAccount account) {
        return new ResponseEntity<>(analyticAccountService.create(account), HttpStatus.CREATED);
    }
}
