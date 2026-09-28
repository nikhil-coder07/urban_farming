package com.aeroponic.farmerp.controller.view;

import com.aeroponic.farmerp.model.Product;
import com.aeroponic.farmerp.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductViewController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String listProducts(@RequestParam(value = "editId", required = false) Long editId, Model model) {
        model.addAttribute("products", productService.getAllProducts());
        if (editId != null) {
            try {
                model.addAttribute("product", productService.getProductById(editId));
            } catch (Exception e) {
                model.addAttribute("product", new Product());
            }
        } else if (!model.containsAttribute("product")) {
            model.addAttribute("product", new Product());
        }
        return "products";
    }

    @PostMapping
    public String saveProduct(@Valid @ModelAttribute("product") Product product,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("products", productService.getAllProducts());
            model.addAttribute("errorMessage", "Please correct the errors in the form.");
            return "products";
        }

        try {
            if (product.getId() == null) {
                productService.createProduct(product);
                redirectAttributes.addFlashAttribute("successMessage", "Product created successfully!");
            } else {
                productService.updateProduct(product.getId(), product);
                redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to save product: " + e.getMessage());
        }
        return "redirect:/products";
    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete product: " + e.getMessage());
        }
        return "redirect:/products";
    }
}
