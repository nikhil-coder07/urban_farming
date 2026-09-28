package com.aeroponic.farmerp.controller.view;

import com.aeroponic.farmerp.dto.PayDTO;
import com.aeroponic.farmerp.dto.PurchaseOrderRequestDTO;
import com.aeroponic.farmerp.model.ContactType;
import com.aeroponic.farmerp.model.PaymentMode;
import com.aeroponic.farmerp.service.ContactService;
import com.aeroponic.farmerp.service.ProductService;
import com.aeroponic.farmerp.service.PurchaseOrderService;
import com.aeroponic.farmerp.service.VendorBillService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/purchases")
public class PurchaseViewController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private VendorBillService vendorBillService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public String listPurchases(Model model) {
        model.addAttribute("purchaseOrders", purchaseOrderService.getAllPurchaseOrders());
        model.addAttribute("vendorBills", vendorBillService.getAllVendorBills());
        model.addAttribute("vendors", contactService.getAllContacts().stream()
                .filter(c -> c.getType() == ContactType.VENDOR)
                .collect(Collectors.toList()));
        model.addAttribute("products", productService.getAllProducts());

        if (!model.containsAttribute("poDto")) {
            model.addAttribute("poDto", new PurchaseOrderRequestDTO());
        }
        return "purchases";
    }

    @PostMapping("/orders")
    public String createPurchaseOrder(@Valid @ModelAttribute("poDto") PurchaseOrderRequestDTO poDto,
                                      BindingResult bindingResult,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid purchase order details.");
            return "redirect:/purchases";
        }

        try {
            purchaseOrderService.createPO(poDto);
            redirectAttributes.addFlashAttribute("successMessage", "Purchase Order created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create PO: " + e.getMessage());
        }
        return "redirect:/purchases";
    }

    @PostMapping("/orders/{id}/convert")
    public String convertToBill(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            purchaseOrderService.convertToBill(id);
            redirectAttributes.addFlashAttribute("successMessage", "PO successfully converted to Vendor Bill with double-entry journal postings!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to convert PO to Bill: " + e.getMessage());
        }
        return "redirect:/purchases";
    }

    @PostMapping("/bills/{id}/pay")
    public String payVendorBill(@PathVariable Long id, @RequestParam("mode") String mode, RedirectAttributes redirectAttributes) {
        try {
            PayDTO payDTO = new PayDTO();
            payDTO.setMode("CASH".equalsIgnoreCase(mode) ? PaymentMode.CASH : PaymentMode.BANK);
            vendorBillService.payVendorBill(id, payDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Vendor Bill paid and ledger updated!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Payment failed: " + e.getMessage());
        }
        return "redirect:/purchases";
    }
}
