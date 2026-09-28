package com.aeroponic.farmerp.service;

import com.aeroponic.farmerp.exception.ResourceNotFoundException;
import com.aeroponic.farmerp.model.ChartOfAccount;
import com.aeroponic.farmerp.repository.ChartOfAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChartOfAccountService {

    @Autowired
    private ChartOfAccountRepository chartOfAccountRepository;

    public List<ChartOfAccount> getAllAccounts() {
        return chartOfAccountRepository.findAll();
    }

    public ChartOfAccount getAccountById(Long id) {
        return chartOfAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));
    }

    public ChartOfAccount createAccount(ChartOfAccount account) {
        return chartOfAccountRepository.save(account);
    }

    public ChartOfAccount updateAccount(Long id, ChartOfAccount details) {
        ChartOfAccount account = getAccountById(id);
        account.setName(details.getName());
        account.setType(details.getType());
        return chartOfAccountRepository.save(account);
    }

    public void deleteAccount(Long id) {
        ChartOfAccount account = getAccountById(id);
        chartOfAccountRepository.delete(account);
    }
}
