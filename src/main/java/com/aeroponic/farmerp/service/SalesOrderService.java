package com.aeroponic.farmerp.service;

import com.aeroponic.farmerp.dto.ConvertToInvoiceDTO;
import com.aeroponic.farmerp.dto.SalesOrderRequestDTO;
import com.aeroponic.farmerp.exception.ResourceNotFoundException;
import com.aeroponic.farmerp.model.*;
import com.aeroponic.farmerp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class SalesOrderService {

    @Autowired
    private SalesOrderRepository salesOrderRepository;

    @Autowired
    private CustomerInvoiceRepository customerInvoiceRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ChartOfAccountRepository chartOfAccountRepository;

    @Autowired
    private LedgerService ledgerService;

    public List<SalesOrder> getAllSalesOrders() {
        return salesOrderRepository.findAll();
    }

    public SalesOrder getById(Long id) {
        return salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales Order not found with ID: " + id));
    }

    public SalesOrder createSO(SalesOrderRequestDTO dto) {
        Contact customer = contactRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + dto.getCustomerId()));

        if (customer.getType() != ContactType.CUSTOMER) {
            throw new IllegalArgumentException("Contact is not a CUSTOMER");
        }

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + dto.getProductId()));

        SalesOrder so = new SalesOrder();
        so.setCustomer(customer);
        so.setProduct(product);
        so.setQty(dto.getQty());
        so.setStatus(SOStatus.PENDING);
        so.setCreatedAt(LocalDate.now());
        return salesOrderRepository.save(so);
    }

    @Transactional
    public CustomerInvoice convertToInvoice(Long soId, ConvertToInvoiceDTO dto) {
        SalesOrder so = getById(soId);
        if (so.getStatus() == SOStatus.INVOICED) {
            throw new IllegalArgumentException("Sales Order is already INVOICED");
        }

        BigDecimal deliveredAmount = so.getProduct().getUnitPrice().multiply(BigDecimal.valueOf(dto.getDeliveredKg()));

        so.setStatus(SOStatus.INVOICED);
        salesOrderRepository.save(so);

        CustomerInvoice invoice = new CustomerInvoice();
        invoice.setSalesOrder(so);
        invoice.setDeliveredKg(dto.getDeliveredKg());
        invoice.setAmount(deliveredAmount);
        invoice.setPaidStatus(PaymentStatus.UNPAID);
        invoice.setInvoiceDate(LocalDate.now());
        CustomerInvoice savedInvoice = customerInvoiceRepository.save(invoice);

        // Double-entry accounting for Customer Invoice creation:
        // DEBIT Asset (Accounts Receivable)
        // CREDIT Income (Produce Sales Revenue)
        ChartOfAccount arAcc = chartOfAccountRepository.findByName("Accounts Receivable")
                .orElseGet(() -> chartOfAccountRepository.findByType(AccountType.ASSET).stream().findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Accounts Receivable account not found")));

        ChartOfAccount incomeAcc = chartOfAccountRepository.findByName("Produce Sales Revenue")
                .orElseGet(() -> chartOfAccountRepository.findByType(AccountType.INCOME).stream().findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Income account not found")));

        ledgerService.postBalancedEntry(
                arAcc,
                incomeAcc,
                deliveredAmount,
                LocalDate.now(),
                "INV-" + savedInvoice.getId(),
                null,
                JournalType.SALES
        );

        return savedInvoice;
    }
}
