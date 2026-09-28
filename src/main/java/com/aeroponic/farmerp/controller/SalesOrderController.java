package com.aeroponic.farmerp.controller;

import com.aeroponic.farmerp.dto.ConvertToInvoiceDTO;
import com.aeroponic.farmerp.dto.SalesOrderRequestDTO;
import com.aeroponic.farmerp.model.CustomerInvoice;
import com.aeroponic.farmerp.model.SalesOrder;
import com.aeroponic.farmerp.service.SalesOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales-orders")
public class SalesOrderController {

    @Autowired
    private SalesOrderService salesOrderService;

    @GetMapping
    public List<SalesOrder> getAllSO() {
        return salesOrderService.getAllSalesOrders();
    }

    @PostMapping
    public ResponseEntity<SalesOrder> createSO(@Valid @RequestBody SalesOrderRequestDTO dto) {
        return new ResponseEntity<>(salesOrderService.createSO(dto), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/convert-to-invoice")
    public ResponseEntity<CustomerInvoice> convertToInvoice(@PathVariable Long id, @Valid @RequestBody ConvertToInvoiceDTO dto) {
        return new ResponseEntity<>(salesOrderService.convertToInvoice(id, dto), HttpStatus.CREATED);
    }
}
