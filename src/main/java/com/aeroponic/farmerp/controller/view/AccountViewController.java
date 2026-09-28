package com.aeroponic.farmerp.controller.view;

import com.aeroponic.farmerp.model.ChartOfAccount;
import com.aeroponic.farmerp.service.ChartOfAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/accounts")
public class AccountViewController {

    @Autowired
    private ChartOfAccountService chartOfAccountService;

    @GetMapping
    public String listAccounts(@RequestParam(value = "editId", required = false) Long editId, Model model) {
        model.addAttribute("accounts", chartOfAccountService.getAllAccounts());
        if (editId != null) {
            try {
                model.addAttribute("account", chartOfAccountService.getAccountById(editId));
            } catch (Exception e) {
                model.addAttribute("account", new ChartOfAccount());
            }
        } else if (!model.containsAttribute("account")) {
            model.addAttribute("account", new ChartOfAccount());
        }
        return "accounts";
    }

    @PostMapping
    public String saveAccount(@Valid @ModelAttribute("account") ChartOfAccount account,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("accounts", chartOfAccountService.getAllAccounts());
            model.addAttribute("errorMessage", "Please correct the errors in the form.");
            return "accounts";
        }

        try {
            if (account.getId() == null) {
                chartOfAccountService.createAccount(account);
                redirectAttributes.addFlashAttribute("successMessage", "Account created successfully!");
            } else {
                chartOfAccountService.updateAccount(account.getId(), account);
                redirectAttributes.addFlashAttribute("successMessage", "Account updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to save account: " + e.getMessage());
        }
        return "redirect:/accounts";
    }

    @PostMapping("/{id}/delete")
    public String deleteAccount(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            chartOfAccountService.deleteAccount(id);
            redirectAttributes.addFlashAttribute("successMessage", "Account deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete account: " + e.getMessage());
        }
        return "redirect:/accounts";
    }
}
