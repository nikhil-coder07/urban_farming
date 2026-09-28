package com.aeroponic.farmerp.controller;

import com.aeroponic.farmerp.dto.PurchaseOrderRequestDTO;
import com.aeroponic.farmerp.model.PurchaseOrder;
import com.aeroponic.farmerp.model.VendorBill;
import com.aeroponic.farmerp.service.PurchaseOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @GetMapping
    public List<PurchaseOrder> getAllPOs() {
        return purchaseOrderService.getAllPurchaseOrders();
    }

    @PostMapping
    public ResponseEntity<PurchaseOrder> createPO(@Valid @RequestBody PurchaseOrderRequestDTO dto) {
        return new ResponseEntity<>(purchaseOrderService.createPO(dto), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/convert-to-bill")
    public ResponseEntity<VendorBill> convertToBill(@PathVariable Long id) {
        return new ResponseEntity<>(purchaseOrderService.convertToBill(id), HttpStatus.CREATED);
    }
}
