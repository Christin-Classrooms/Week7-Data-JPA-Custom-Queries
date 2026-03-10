package com.example.Thymeleaf.Demo.controllers;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.Service.FighterService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FighterController {

    private final FighterService fighterService;

    public FighterController(FighterService fighterService) {
        this.fighterService = fighterService;
    }

    @GetMapping("/fighters")
    public String getFighters(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "all") String filterType
    ) {

        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("DESC")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<Fighter> fighterPage;

        switch (filterType.toLowerCase()) {

            case "name":
                if (search != null && !search.isEmpty()) {
                    fighterPage = fighterService.findByNameContainingIgnoreCase(search, pageable);
                } else {
                    fighterPage = fighterService.getAllFighters(pageable);
                }
                break;

            case "health":
                int minHealth = 1200;
                try {
                    if (search != null && !search.isEmpty()) {
                        minHealth = Integer.parseInt(search);
                    }
                } catch (NumberFormatException ignored) {}
                fighterPage = fighterService.findByHealthGreaterThan(minHealth, pageable);
                break;

            case "strongest":
                fighterPage = fighterService.findStrongestFighters(pageable);
                break;

            case "balanced":
                fighterPage = fighterService.findBalancedFighters(1200, 60, pageable);
                break;

            default:
                fighterPage = fighterService.getAllFighters(pageable);
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