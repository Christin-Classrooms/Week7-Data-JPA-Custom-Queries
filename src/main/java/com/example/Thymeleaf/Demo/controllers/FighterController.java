package com.example.Thymeleaf.Demo.controllers;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.Service.FighterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

@Controller
public class FighterController {

    private final FighterService fighterService;

    public FighterController(FighterService fighterService) {
        this.fighterService = fighterService;
    }

    @GetMapping("/fighters")
    public String getFighters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "all") String filterType,
            Model model) {
        
        Page<Fighter> fighterPage;
        
        // Determine which query to use based on filterType
        switch (filterType) {
            case "name":
                fighterPage = fighterService.searchFightersByName(search, page, size, sort, direction);
                break;
            case "health":
                try {
                    int healthValue = search != null ? Integer.parseInt(search) : 1000;
                    fighterPage = fighterService.getFightersByHealthGreaterThan(healthValue, page, size, sort, direction);
                } catch (NumberFormatException e) {
                    // If search is not a valid number, default to health > 1000
                    fighterPage = fighterService.getFightersByHealthGreaterThan(1000, page, size, sort, direction);
                }
                break;
            case "strongest":
                fighterPage = fighterService.getStrongestFighters(page, size);
                break;
            case "balanced":
                // For balanced fighters, we need to parse two values from search or use defaults
                double minHealth = 1000;
                double maxDamage = 50;
                
                if (search != null && !search.isEmpty()) {
                    String[] values = search.split(",");
                    try {
                        if (values.length >= 1) {
                            minHealth = Double.parseDouble(values[0].trim());
                        }
                        if (values.length >= 2) {
                            maxDamage = Double.parseDouble(values[1].trim());
                        }
                    } catch (NumberFormatException e) {
                        // Use defaults if parsing fails
                    }
                }
                
                fighterPage = fighterService.getBalancedFighters(minHealth, maxDamage, page, size, sort, direction);
                break;
            default: // "all" or any other value
                fighterPage = fighterService.getAllFightersPaginated(page, size, sort, direction);
                break;
        }
        
        // Add attributes to model
        model.addAttribute("fighters", fighterPage.getContent());
        model.addAttribute("totalPages", fighterPage.getTotalPages());
        model.addAttribute("totalElements", fighterPage.getTotalElements());
        model.addAttribute("currentPage", fighterPage.getNumber());
        model.addAttribute("pageSize", fighterPage.getSize());
        model.addAttribute("hasPrevious", fighterPage.hasPrevious());
        model.addAttribute("hasNext", fighterPage.hasNext());
        
        // Add filter parameters to model for maintaining state in view
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("filterType", filterType);
        
        return "Fighters";
    }
}