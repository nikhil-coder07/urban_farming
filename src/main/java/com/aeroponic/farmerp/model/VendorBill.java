package com.aeroponic.farmerp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "vendor_bills")
public class VendorBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "po_id", nullable = false)
    private PurchaseOrder purchaseOrder;

    @Column(precision = 14, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "paid_status", nullable = false)
    private PaymentStatus paidStatus = PaymentStatus.UNPAID;

    @Column(name = "bill_date", nullable = false)
    private LocalDate billDate = LocalDate.now();

    public VendorBill() {}

    public VendorBill(Long id, PurchaseOrder purchaseOrder, BigDecimal amount, PaymentStatus paidStatus, LocalDate billDate) {
        this.id = id;
        this.purchaseOrder = purchaseOrder;
        this.amount = amount;
        this.paidStatus = paidStatus != null ? paidStatus : PaymentStatus.UNPAID;
        this.billDate = billDate != null ? billDate : LocalDate.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public PurchaseOrder getPurchaseOrder() { return purchaseOrder; }
    public void setPurchaseOrder(PurchaseOrder purchaseOrder) { this.purchaseOrder = purchaseOrder; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public PaymentStatus getPaidStatus() { return paidStatus; }
    public void setPaidStatus(PaymentStatus paidStatus) { this.paidStatus = paidStatus; }
    public LocalDate getBillDate() { return billDate; }
    public void setBillDate(LocalDate billDate) { this.billDate = billDate; }
}
