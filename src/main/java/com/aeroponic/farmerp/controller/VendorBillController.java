package com.aeroponic.farmerp.controller;

import com.aeroponic.farmerp.dto.PayDTO;
import com.aeroponic.farmerp.model.Payment;
import com.aeroponic.farmerp.model.VendorBill;
import com.aeroponic.farmerp.service.VendorBillService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor-bills")
public class VendorBillController {

    @Autowired
    private VendorBillService vendorBillService;

    @GetMapping
    public List<VendorBill> getAllVendorBills() {
        return vendorBillService.getAllVendorBills();
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<Payment> payVendorBill(@PathVariable Long id, @Valid @RequestBody PayDTO payDTO) {
        return new ResponseEntity<>(vendorBillService.payVendorBill(id, payDTO), HttpStatus.OK);
    }
}
