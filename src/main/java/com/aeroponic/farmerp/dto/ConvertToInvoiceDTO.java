package com.aeroponic.farmerp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class ConvertToInvoiceDTO {

    @NotNull(message = "Delivered KG is required")
    @DecimalMin(value = "0.01", message = "Delivered KG must be greater than zero")
    private Double deliveredKg;

    public Double getDeliveredKg() { return deliveredKg; }
    public void setDeliveredKg(Double deliveredKg) { this.deliveredKg = deliveredKg; }
}
