package com.aeroponic.farmerp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ref_type", length = 20, nullable = false)
    private String refType; // 'INVOICE' or 'BILL'

    @Column(name = "ref_id", nullable = false)
    private Long refId;

    @Column(precision = 14, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(length = 20, nullable = false)
    private String mode; // 'BANK' or 'CASH'

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate = LocalDate.now();

    public Payment() {}

    public Payment(Long id, String refType, Long refId, BigDecimal amount, String mode, LocalDate paymentDate) {
        this.id = id;
        this.refType = refType;
        this.refId = refId;
        this.amount = amount;
        this.mode = mode;
        this.paymentDate = paymentDate != null ? paymentDate : LocalDate.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRefType() { return refType; }
    public void setRefType(String refType) { this.refType = refType; }
    public Long getRefId() { return refId; }
    public void setRefId(Long refId) { this.refId = refId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
    public LocalDate getDate() { return paymentDate; }
    public void setDate(LocalDate date) { this.paymentDate = date; }
}
