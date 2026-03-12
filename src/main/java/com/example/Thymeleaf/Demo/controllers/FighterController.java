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

import java.util.List;

@Controller
public class FighterController {

    private final FighterService fighterService;

    public FighterController(FighterService fighterService) {
        this.fighterService = fighterService;
    }

    @GetMapping("/fighters")
    public String getFighters(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "filterType", required = false, defaultValue = "all") String filterType,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "ASC") String direction,
            Model model) {

        List<String> allowedSorts = List.of("id", "name", "health", "damage", "resistance");
        if (!allowedSorts.contains(sortBy)) {
            sortBy = "id";
        }

        Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<Fighter> fighterPage;

        if (filterType.equalsIgnoreCase("name") && !search.trim().isEmpty()) {
            fighterPage = fighterService.findFighterByName(search, pageable);
        } else if (filterType.equalsIgnoreCase("health")) {
            fighterPage = fighterService.findFightersByHealth(1200, pageable);
        } else if (filterType.equalsIgnoreCase("strongest")) {
            fighterPage = fighterService.findStrongestFighters(pageable);
        } else if (filterType.equalsIgnoreCase("balanced")) {
            fighterPage = fighterService.findBalancedFighters(1200, 85.0, pageable);
        } else {
            fighterPage = fighterService.getAllFightersPageable(pageable);
        }

        model.addAttribute("fighters", fighterPage.getContent());
        model.addAttribute("totalPages", fighterPage.getTotalPages());
        model.addAttribute("totalElements", fighterPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("hasPrevious", fighterPage.hasPrevious());
        model.addAttribute("hasNext", fighterPage.hasNext());
        model.addAttribute("search", search);
        model.addAttribute("sort", sortBy);
        model.addAttribute("direction", sortDirection.name());
        model.addAttribute("filterType", filterType);

        return "Fighters";
    }
}