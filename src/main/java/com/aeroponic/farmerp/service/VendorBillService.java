package com.aeroponic.farmerp.service;

import com.aeroponic.farmerp.dto.PayDTO;
import com.aeroponic.farmerp.exception.ResourceNotFoundException;
import com.aeroponic.farmerp.model.*;
import com.aeroponic.farmerp.repository.ChartOfAccountRepository;
import com.aeroponic.farmerp.repository.PaymentRepository;
import com.aeroponic.farmerp.repository.VendorBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class VendorBillService {

    @Autowired
    private VendorBillRepository vendorBillRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ChartOfAccountRepository chartOfAccountRepository;

    @Autowired
    private LedgerService ledgerService;

    public List<VendorBill> getAllVendorBills() {
        return vendorBillRepository.findAll();
    }

    public VendorBill getById(Long id) {
        return vendorBillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor Bill not found with ID: " + id));
    }

    @Transactional
    public Payment payVendorBill(Long billId, PayDTO payDTO) {
        VendorBill bill = getById(billId);
        if (bill.getPaidStatus() == PaymentStatus.PAID) {
            throw new IllegalArgumentException("Vendor Bill is already PAID");
        }

        bill.setPaidStatus(PaymentStatus.PAID);
        vendorBillRepository.save(bill);

        String modeStr = payDTO.getMode() != null ? payDTO.getMode().name() : "BANK";

        Payment payment = new Payment();
        payment.setRefType("BILL");
        payment.setRefId(bill.getId());
        payment.setAmount(bill.getAmount());
        payment.setMode(modeStr);
        payment.setPaymentDate(LocalDate.now());
        Payment savedPayment = paymentRepository.save(payment);

        // Double-entry accounting for Vendor Bill payment:
        // DEBIT Liability (Mineral Supplier Payables)
        // CREDIT Asset (Cash/Bank)
        ChartOfAccount liabilityAcc = chartOfAccountRepository.findByName("Mineral Supplier Payables")
                .orElseGet(() -> chartOfAccountRepository.findByType(AccountType.LIABILITY).stream().findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Liability account not found")));

        ChartOfAccount cashAssetAcc = chartOfAccountRepository.findByName("Cash/Bank")
                .orElseGet(() -> chartOfAccountRepository.findByType(AccountType.ASSET).stream().findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Asset account not found")));

        JournalType journalType = "CASH".equalsIgnoreCase(modeStr) ? JournalType.CASH : JournalType.BANK;

        ledgerService.postBalancedEntry(
                liabilityAcc,
                cashAssetAcc,
                bill.getAmount(),
                LocalDate.now(),
                "PAY-BILL-" + bill.getId(),
                null,
                journalType
        );

        return savedPayment;
    }
}
