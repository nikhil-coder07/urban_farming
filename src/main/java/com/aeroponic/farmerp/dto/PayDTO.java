package com.aeroponic.farmerp.dto;

import com.aeroponic.farmerp.model.PaymentMode;
import jakarta.validation.constraints.NotNull;

public class PayDTO {

    @NotNull(message = "Payment mode is required (CASH or BANK)")
    private PaymentMode mode;

    public PaymentMode getMode() { return mode; }
    public void setMode(PaymentMode mode) { this.mode = mode; }
}
