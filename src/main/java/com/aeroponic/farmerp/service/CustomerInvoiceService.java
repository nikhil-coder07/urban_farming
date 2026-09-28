package com.aeroponic.farmerp.service;

import com.aeroponic.farmerp.dto.PayDTO;
import com.aeroponic.farmerp.exception.ResourceNotFoundException;
import com.aeroponic.farmerp.model.*;
import com.aeroponic.farmerp.repository.ChartOfAccountRepository;
import com.aeroponic.farmerp.repository.CustomerInvoiceRepository;
import com.aeroponic.farmerp.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CustomerInvoiceService {

    @Autowired
    private CustomerInvoiceRepository customerInvoiceRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ChartOfAccountRepository chartOfAccountRepository;

    @Autowired
    private LedgerService ledgerService;

    public List<CustomerInvoice> getAllInvoices() {
        return customerInvoiceRepository.findAll();
    }

    public CustomerInvoice getById(Long id) {
        return customerInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Invoice not found with ID: " + id));
    }

    @Transactional
    public Payment payInvoice(Long invoiceId, PayDTO payDTO) {
        CustomerInvoice invoice = getById(invoiceId);
        if (invoice.getPaidStatus() == PaymentStatus.PAID) {
            throw new IllegalArgumentException("Customer Invoice is already PAID");
        }

        invoice.setPaidStatus(PaymentStatus.PAID);
        customerInvoiceRepository.save(invoice);

        String modeStr = payDTO.getMode() != null ? payDTO.getMode().name() : "BANK";

        Payment payment = new Payment();
        payment.setRefType("INVOICE");
        payment.setRefId(invoice.getId());
        payment.setAmount(invoice.getAmount());
        payment.setMode(modeStr);
        payment.setPaymentDate(LocalDate.now());
        Payment savedPayment = paymentRepository.save(payment);

        // Double-entry accounting for Customer Invoice payment:
        // DEBIT Asset (Cash/Bank)
        // CREDIT Asset (Accounts Receivable)
        ChartOfAccount cashAssetAcc = chartOfAccountRepository.findByName("Cash/Bank")
                .orElseGet(() -> chartOfAccountRepository.findByType(AccountType.ASSET).stream().findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Asset account not found")));

        ChartOfAccount arAcc = chartOfAccountRepository.findByName("Accounts Receivable")
                .orElseGet(() -> chartOfAccountRepository.findByType(AccountType.ASSET).stream().filter(a -> a.getName().contains("Receivable")).findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Accounts Receivable account not found")));

        JournalType journalType = "CASH".equalsIgnoreCase(modeStr) ? JournalType.CASH : JournalType.BANK;

        ledgerService.postBalancedEntry(
                cashAssetAcc,
                arAcc,
                invoice.getAmount(),
                LocalDate.now(),
                "PAY-INV-" + invoice.getId(),
                null,
                journalType
        );

        return savedPayment;
    }
}
