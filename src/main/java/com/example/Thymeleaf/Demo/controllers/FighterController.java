package com.example.Thymeleaf.Demo.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.Service.FighterService;

@Controller
public class FighterController {

    private final FighterService fighterService;

    public FighterController(FighterService fighterService) {
        this.fighterService = fighterService;
    }

    @GetMapping({"/fighters", "/Fighters"})
    public String getFighters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "") String filterType,
            Model model) {

        Sort sortOrder = Sort.by(Sort.Direction.fromString(direction), sort);
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Page<Fighter> fighterPage;

        if (filterType == null || filterType.isEmpty()) {
            fighterPage = fighterService.findAll(pageable);
        } else {
            switch (filterType) {
                case "name":
                    if (!search.isEmpty()) {
                        fighterPage = fighterService.findByNameContainingIgnoreCase(search, pageable);
                    } else {
                        fighterPage = fighterService.findAll(pageable);
                    }
                    break;
                case "health":
                    try {
                        int healthValue = Integer.parseInt(search);
                        fighterPage = fighterService.findByHealthGreaterThanEqual(healthValue, pageable);
                    } catch (NumberFormatException e) {
                        fighterPage = fighterService.findAll(pageable);
                    }
                    break;
                case "strongest":
                    fighterPage = fighterService.findStrongestFighters(pageable);
                    break;
                case "balanced":
                    double maxHealth = 1000;
                    double maxDamage = 500;
                    fighterPage = fighterService.findBalancedFighters(maxHealth, maxDamage, pageable);
                    break;
                default:
                    fighterPage = fighterService.findAll(pageable);
            }
        }

        model.addAttribute("fighters", fighterPage.getContent());
        model.addAttribute("totalPages", fighterPage.getTotalPages());
        model.addAttribute("totalElements", fighterPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("hasPrevious", fighterPage.hasPrevious());
        model.addAttribute("hasNext", fighterPage.hasNext());
        model.addAttribute("search", search == null ? "" : search);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("filterType", filterType);

        return "Fighters";
    }
}
