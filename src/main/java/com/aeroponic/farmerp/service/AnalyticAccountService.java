package com.aeroponic.farmerp.service;

import com.aeroponic.farmerp.exception.ResourceNotFoundException;
import com.aeroponic.farmerp.model.AnalyticAccount;
import com.aeroponic.farmerp.repository.AnalyticAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticAccountService {

    @Autowired
    private AnalyticAccountRepository analyticAccountRepository;

    public List<AnalyticAccount> getAllAnalyticAccounts() {
        return analyticAccountRepository.findAll();
    }

    public AnalyticAccount getById(Long id) {
        return analyticAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Analytic Account not found with ID: " + id));
    }

    public AnalyticAccount create(AnalyticAccount account) {
        return analyticAccountRepository.save(account);
    }
}
