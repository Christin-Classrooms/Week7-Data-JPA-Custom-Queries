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

import java.util.Set;

@Controller
public class FighterController {
    private static final Set<String> ALLOWED_SORTS = Set.of("id", "name", "health", "damage", "resistance");
    private static final int HEALTH_FILTER_VALUE = 1200;
    private static final double BALANCED_DAMAGE_VALUE = 50.0;

    private final FighterService fighterService;

    public FighterController(FighterService fighterService) {
        this.fighterService = fighterService;
    }

    @GetMapping("/fighters")
    public String getFighters(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction,
            @RequestParam(name = "search", defaultValue = "") String search,
            @RequestParam(name = "filterType", defaultValue = "all") String filterType,
            Model model) {

        String safeSort = ALLOWED_SORTS.contains(sortBy) ? sortBy : "id";
        Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, safeSort));

        Page<Fighter> fighterPage;
        String currentSort = safeSort;
        String currentDirection = sortDirection.name();
        String currentFilterType = filterType == null || filterType.isBlank() ? "all" : filterType;

        switch (currentFilterType) {
            case "name":
                if (search != null && !search.isBlank()) {
                    fighterPage = fighterService.findFightersByName(search, pageable);
                } else {
                    fighterPage = fighterService.getAllFightersPageable(pageable);
                    currentFilterType = "all";
                }
                break;
            case "health":
                fighterPage = fighterService.findFightersByHealth(HEALTH_FILTER_VALUE, pageable);
                break;
            case "strongest":
                fighterPage = fighterService.findStrongestFighters(PageRequest.of(page, size));
                currentSort = "damage";
                currentDirection = "DESC";
                break;
            case "balanced":
                fighterPage = fighterService.findBalancedFighters(HEALTH_FILTER_VALUE, BALANCED_DAMAGE_VALUE, PageRequest.of(page, size));
                currentSort = "resistance";
                currentDirection = "DESC";
                break;
            default:
                fighterPage = fighterService.getAllFightersPageable(pageable);
                currentFilterType = "all";
                break;
        }

        model.addAttribute("fighters", fighterPage.getContent());
        model.addAttribute("totalPages", fighterPage.getTotalPages());
        model.addAttribute("totalElements", fighterPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("hasPrevious", fighterPage.hasPrevious());
        model.addAttribute("hasNext", fighterPage.hasNext());
        model.addAttribute("search", search);
        model.addAttribute("sort", currentSort);
        model.addAttribute("direction", currentDirection);
        model.addAttribute("filterType", currentFilterType);
        return "Fighters";
    }
}
