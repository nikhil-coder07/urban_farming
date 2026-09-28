package com.aeroponic.farmerp.service;

import com.aeroponic.farmerp.dto.BudgetRequestDTO;
import com.aeroponic.farmerp.exception.ResourceNotFoundException;
import com.aeroponic.farmerp.model.AnalyticAccount;
import com.aeroponic.farmerp.model.Budget;
import com.aeroponic.farmerp.repository.AnalyticAccountRepository;
import com.aeroponic.farmerp.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private AnalyticAccountRepository analyticAccountRepository;

    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    public Budget createBudget(BudgetRequestDTO dto) {
        AnalyticAccount aa = analyticAccountRepository.findById(dto.getAnalyticAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Analytic Account not found with ID: " + dto.getAnalyticAccountId()));

        Budget budget = new Budget();
        budget.setAnalyticAccount(aa);
        budget.setAllocatedAmount(dto.getAllocatedAmount());
        budget.setPeriod(dto.getPeriod());
        return budgetRepository.save(budget);
    }
}
