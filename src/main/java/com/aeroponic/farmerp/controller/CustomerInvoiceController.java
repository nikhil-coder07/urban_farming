package com.aeroponic.farmerp.controller;

import com.aeroponic.farmerp.dto.PayDTO;
import com.aeroponic.farmerp.model.CustomerInvoice;
import com.aeroponic.farmerp.model.Payment;
import com.aeroponic.farmerp.service.CustomerInvoiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer-invoices")
public class CustomerInvoiceController {

    @Autowired
    private CustomerInvoiceService customerInvoiceService;

    @GetMapping
    public List<CustomerInvoice> getAllInvoices() {
        return customerInvoiceService.getAllInvoices();
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<Payment> payInvoice(@PathVariable Long id, @Valid @RequestBody PayDTO payDTO) {
        return new ResponseEntity<>(customerInvoiceService.payInvoice(id, payDTO), HttpStatus.OK);
    }
}
