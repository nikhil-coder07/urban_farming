package com.aeroponic.farmerp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    @Column(length = 150, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType type = ProductType.GOODS;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.01", message = "Unit price must be at least 0.01")
    @Column(name = "unit_price", precision = 12, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @NotBlank(message = "Unit of measure is required (e.g. KG or UNIT)")
    @Column(length = 10, nullable = false)
    private String uom; // 'KG' or 'UNIT'

    public Product() {}

    public Product(Long id, String name, ProductType type, BigDecimal unitPrice, String uom) {
        this.id = id;
        this.name = name;
        this.type = type != null ? type : ProductType.GOODS;
        this.unitPrice = unitPrice;
        this.uom = uom;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public ProductType getType() { return type; }
    public void setType(ProductType type) { this.type = type; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public String getUom() { return uom; }
    public void setUom(String uom) { this.uom = uom; }
}
