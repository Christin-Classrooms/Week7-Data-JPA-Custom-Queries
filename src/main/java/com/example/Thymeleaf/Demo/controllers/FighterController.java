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

    @GetMapping("/fighters")
    public String getFighters(
        // page, sorting, and search parameters
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "all") String filterType,
            Model model) 
            {

    Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC")
        ? Sort.Direction.DESC
        : Sort.Direction.ASC;

    Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        Page<Fighter> fighterPage;

    // filter available for name
    switch (filterType) {
        case "name":
            fighterPage = fighterService.findByNameContainingIgnoreCase(search, pageable);
            break;

    // health filter
        case "health":
            int health;
            try {
                health = Integer.parseInt(search);
            } catch (NumberFormatException e) {
                health = 0;
            }
            fighterPage = fighterService.findByHealthGreaterThan(health, pageable);
        break;

    // strongest fighter
        case "strongest":
            fighterPage = fighterService.findStrongestFighters(pageable);
            break;

    // balance filter
        case "balanced":
            fighterPage = fighterService.findBalancedFighters(1000.0, 50.0, pageable);
            break;

    // default, show ALL
        default:
            fighterPage = fighterService.findAll(pageable);
        }
        model.addAttribute("fighters", fighterPage.getContent());
        model.addAttribute("totalPages", fighterPage.getTotalPages());
        model.addAttribute("totalElements", fighterPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("hasPrevious", fighterPage.hasPrevious());
        model.addAttribute("hasNext", fighterPage.hasNext());
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("filterType", filterType);
        return "Fighters";
    }
}