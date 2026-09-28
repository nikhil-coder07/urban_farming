package com.aeroponic.farmerp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sales_orders")
public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Contact customer;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer qty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SOStatus status = SOStatus.PENDING;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt = LocalDate.now();

    public SalesOrder() {}

    public SalesOrder(Long id, Contact customer, Product product, Integer qty, SOStatus status, LocalDate createdAt) {
        this.id = id;
        this.customer = customer;
        this.product = product;
        this.qty = qty;
        this.status = status != null ? status : SOStatus.PENDING;
        this.createdAt = createdAt != null ? createdAt : LocalDate.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Contact getCustomer() { return customer; }
    public void setCustomer(Contact customer) { this.customer = customer; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }
    public SOStatus getStatus() { return status; }
    public void setStatus(SOStatus status) { this.status = status; }
    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
}
