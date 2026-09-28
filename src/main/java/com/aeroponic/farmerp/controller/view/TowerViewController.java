package com.aeroponic.farmerp.controller.view;

import com.aeroponic.farmerp.dto.TelemetryRequestDTO;
import com.aeroponic.farmerp.model.Tower;
import com.aeroponic.farmerp.service.TelemetryService;
import com.aeroponic.farmerp.service.TowerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/towers")
public class TowerViewController {

    @Autowired
    private TowerService towerService;

    @Autowired
    private TelemetryService telemetryService;

    @GetMapping
    public String listTowers(Model model) {
        model.addAttribute("towers", towerService.getAllTowers());
        model.addAttribute("telemetryList", telemetryService.getAllTelemetry());
        if (!model.containsAttribute("newTower")) {
            model.addAttribute("newTower", new Tower());
        }
        if (!model.containsAttribute("telemetryDto")) {
            model.addAttribute("telemetryDto", new TelemetryRequestDTO());
        }
        return "towers";
    }

    @PostMapping
    public String createTower(@Valid @ModelAttribute("newTower") Tower tower,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("towers", towerService.getAllTowers());
            model.addAttribute("telemetryList", telemetryService.getAllTelemetry());
            model.addAttribute("telemetryDto", new TelemetryRequestDTO());
            model.addAttribute("errorMessage", "Please fix errors in tower form.");
            return "towers";
        }

        try {
            towerService.createTower(tower);
            redirectAttributes.addFlashAttribute("successMessage", "Tower created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create tower: " + e.getMessage());
        }
        return "redirect:/towers";
    }

    @PostMapping("/telemetry")
    public String logTelemetry(@Valid @ModelAttribute("telemetryDto") TelemetryRequestDTO telemetryDto,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("towers", towerService.getAllTowers());
            model.addAttribute("telemetryList", telemetryService.getAllTelemetry());
            model.addAttribute("newTower", new Tower());
            model.addAttribute("errorMessage", "Please fix errors in telemetry form.");
            return "towers";
        }

        try {
            telemetryService.recordTelemetry(telemetryDto);
            redirectAttributes.addFlashAttribute("successMessage", "Telemetry reading recorded successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to record telemetry: " + e.getMessage());
        }
        return "redirect:/towers";
    }

    @PostMapping("/{id}/delete")
    public String deleteTower(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            towerService.deleteTower(id);
            redirectAttributes.addFlashAttribute("successMessage", "Tower deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete tower: " + e.getMessage());
        }
        return "redirect:/towers";
    }
}
