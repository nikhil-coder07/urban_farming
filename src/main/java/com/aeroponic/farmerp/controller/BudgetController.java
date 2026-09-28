package com.aeroponic.farmerp.controller;

import com.aeroponic.farmerp.dto.BudgetRequestDTO;
import com.aeroponic.farmerp.model.Budget;
import com.aeroponic.farmerp.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping
    public List<Budget> getAllBudgets() {
        return budgetService.getAllBudgets();
    }

    @PostMapping
    public ResponseEntity<Budget> createBudget(@Valid @RequestBody BudgetRequestDTO dto) {
        return new ResponseEntity<>(budgetService.createBudget(dto), HttpStatus.CREATED);
    }
}
