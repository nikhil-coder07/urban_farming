package com.aeroponic.farmerp.controller.view;

import com.aeroponic.farmerp.dto.ConvertToInvoiceDTO;
import com.aeroponic.farmerp.dto.PayDTO;
import com.aeroponic.farmerp.dto.SalesOrderRequestDTO;
import com.aeroponic.farmerp.model.ContactType;
import com.aeroponic.farmerp.model.PaymentMode;
import com.aeroponic.farmerp.service.ContactService;
import com.aeroponic.farmerp.service.CustomerInvoiceService;
import com.aeroponic.farmerp.service.ProductService;
import com.aeroponic.farmerp.service.SalesOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/sales")
public class SalesViewController {

    @Autowired
    private SalesOrderService salesOrderService;

    @Autowired
    private CustomerInvoiceService customerInvoiceService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public String listSales(Model model) {
        model.addAttribute("salesOrders", salesOrderService.getAllSalesOrders());
        model.addAttribute("customerInvoices", customerInvoiceService.getAllInvoices());
        model.addAttribute("customers", contactService.getAllContacts().stream()
                .filter(c -> c.getType() == ContactType.CUSTOMER)
                .collect(Collectors.toList()));
        model.addAttribute("products", productService.getAllProducts());

        if (!model.containsAttribute("soDto")) {
            model.addAttribute("soDto", new SalesOrderRequestDTO());
        }
        return "sales";
    }

    @PostMapping("/orders")
    public String createSalesOrder(@Valid @ModelAttribute("soDto") SalesOrderRequestDTO soDto,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid sales order details.");
            return "redirect:/sales";
        }

        try {
            salesOrderService.createSO(soDto);
            redirectAttributes.addFlashAttribute("successMessage", "Sales Order created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create SO: " + e.getMessage());
        }
        return "redirect:/sales";
    }

    @PostMapping("/orders/{id}/convert")
    public String convertToInvoice(@PathVariable Long id,
                                   @RequestParam("deliveredKg") Double deliveredKg,
                                   RedirectAttributes redirectAttributes) {
        try {
            ConvertToInvoiceDTO dto = new ConvertToInvoiceDTO();
            dto.setDeliveredKg(deliveredKg);
            salesOrderService.convertToInvoice(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "SO converted to Customer Invoice with revenue recognized!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to convert SO to Invoice: " + e.getMessage());
        }
        return "redirect:/sales";
    }

    @PostMapping("/invoices/{id}/pay")
    public String payInvoice(@PathVariable Long id,
                             @RequestParam("mode") String mode,
                             RedirectAttributes redirectAttributes) {
        try {
            PayDTO payDTO = new PayDTO();
            payDTO.setMode("CASH".equalsIgnoreCase(mode) ? PaymentMode.CASH : PaymentMode.BANK);
            customerInvoiceService.payInvoice(id, payDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Payment received and Accounts Receivable cleared!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Payment recording failed: " + e.getMessage());
        }
        return "redirect:/sales";
    }
}
