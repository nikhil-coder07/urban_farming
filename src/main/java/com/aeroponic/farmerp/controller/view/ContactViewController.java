package com.aeroponic.farmerp.controller.view;

import com.aeroponic.farmerp.model.Contact;
import com.aeroponic.farmerp.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/contacts")
public class ContactViewController {

    @Autowired
    private ContactService contactService;

    @GetMapping
    public String listContacts(@RequestParam(value = "editId", required = false) Long editId, Model model) {
        model.addAttribute("contacts", contactService.getAllContacts());
        if (editId != null) {
            try {
                model.addAttribute("contact", contactService.getContactById(editId));
            } catch (Exception e) {
                model.addAttribute("contact", new Contact());
            }
        } else if (!model.containsAttribute("contact")) {
            model.addAttribute("contact", new Contact());
        }
        return "contacts";
    }

    @PostMapping
    public String saveContact(@Valid @ModelAttribute("contact") Contact contact,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("contacts", contactService.getAllContacts());
            model.addAttribute("errorMessage", "Please correct the errors in the form.");
            return "contacts";
        }

        try {
            if (contact.getId() == null) {
                contactService.createContact(contact);
                redirectAttributes.addFlashAttribute("successMessage", "Contact added successfully!");
            } else {
                contactService.updateContact(contact.getId(), contact);
                redirectAttributes.addFlashAttribute("successMessage", "Contact updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to save contact: " + e.getMessage());
        }
        return "redirect:/contacts";
    }

    @PostMapping("/{id}/delete")
    public String deleteContact(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            contactService.deleteContact(id);
            redirectAttributes.addFlashAttribute("successMessage", "Contact deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete contact: " + e.getMessage());
        }
        return "redirect:/contacts";
    }
}
