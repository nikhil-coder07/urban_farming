package com.aeroponic.farmerp.service;

import com.aeroponic.farmerp.dto.PurchaseOrderRequestDTO;
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
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private VendorBillRepository vendorBillRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ChartOfAccountRepository chartOfAccountRepository;

    @Autowired
    private LedgerService ledgerService;

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    public PurchaseOrder getById(Long id) {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with ID: " + id));
    }

    public PurchaseOrder createPO(PurchaseOrderRequestDTO dto) {
        Contact vendor = contactRepository.findById(dto.getVendorId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with ID: " + dto.getVendorId()));

        if (vendor.getType() != ContactType.VENDOR) {
            throw new IllegalArgumentException("Contact is not a VENDOR");
        }

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + dto.getProductId()));

        PurchaseOrder po = new PurchaseOrder();
        po.setVendor(vendor);
        po.setProduct(product);
        po.setQty(dto.getQty());
        po.setStatus(POStatus.PENDING);
        po.setCreatedAt(LocalDate.now());
        return purchaseOrderRepository.save(po);
    }

    @Transactional
    public VendorBill convertToBill(Long poId) {
        PurchaseOrder po = getById(poId);
        if (po.getStatus() == POStatus.BILLED) {
            throw new IllegalArgumentException("Purchase Order is already BILLED");
        }

        BigDecimal amount = po.getProduct().getUnitPrice().multiply(BigDecimal.valueOf(po.getQty()));

        po.setStatus(POStatus.BILLED);
        purchaseOrderRepository.save(po);

        VendorBill bill = new VendorBill();
        bill.setPurchaseOrder(po);
        bill.setAmount(amount);
        bill.setPaidStatus(PaymentStatus.UNPAID);
        bill.setBillDate(LocalDate.now());
        VendorBill savedBill = vendorBillRepository.save(bill);

        // Double-entry accounting for Vendor Bill creation:
        // DEBIT Expense (e.g. Nutrient Chemical Costs)
        // CREDIT Liability (e.g. Mineral Supplier Payables)
        ChartOfAccount expenseAcc = chartOfAccountRepository.findByName("Nutrient Chemical Costs")
                .orElseGet(() -> chartOfAccountRepository.findByType(AccountType.EXPENSE).stream().findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Expense account not found")));

        ChartOfAccount liabilityAcc = chartOfAccountRepository.findByName("Mineral Supplier Payables")
                .orElseGet(() -> chartOfAccountRepository.findByType(AccountType.LIABILITY).stream().findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Liability account not found")));

        ledgerService.postBalancedEntry(
                expenseAcc,
                liabilityAcc,
                amount,
                LocalDate.now(),
                "BILL-" + savedBill.getId(),
                null,
                JournalType.PURCHASE
        );

        return savedBill;
    }
}
