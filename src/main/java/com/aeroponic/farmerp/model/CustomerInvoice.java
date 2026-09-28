package com.aeroponic.farmerp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "customer_invoices")
public class CustomerInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "so_id", nullable = false)
    private SalesOrder salesOrder;

    @Column(name = "delivered_kg", nullable = false)
    private Double deliveredKg;

    @Column(precision = 14, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "paid_status", nullable = false)
    private PaymentStatus paidStatus = PaymentStatus.UNPAID;

    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate = LocalDate.now();

    public CustomerInvoice() {}

    public CustomerInvoice(Long id, SalesOrder salesOrder, Double deliveredKg, BigDecimal amount, PaymentStatus paidStatus, LocalDate invoiceDate) {
        this.id = id;
        this.salesOrder = salesOrder;
        this.deliveredKg = deliveredKg;
        this.amount = amount;
        this.paidStatus = paidStatus != null ? paidStatus : PaymentStatus.UNPAID;
        this.invoiceDate = invoiceDate != null ? invoiceDate : LocalDate.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public SalesOrder getSalesOrder() { return salesOrder; }
    public void setSalesOrder(SalesOrder salesOrder) { this.salesOrder = salesOrder; }
    public Double getDeliveredKg() { return deliveredKg; }
    public void setDeliveredKg(Double deliveredKg) { this.deliveredKg = deliveredKg; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public PaymentStatus getPaidStatus() { return paidStatus; }
    public void setPaidStatus(PaymentStatus paidStatus) { this.paidStatus = paidStatus; }
    public LocalDate getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }
}
