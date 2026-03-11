package com.example.Thymeleaf.Demo.controllers;

import java.util.List;

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

    private static final List<String> ALLOWED_SORT_FIELDS = List.of("id", "name", "health", "damage", "resistance");

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
            @RequestParam(defaultValue = "all") String filterType,
            Model model) {

        if (!ALLOWED_SORT_FIELDS.contains(sort)) {
            sort = "id";
        }

        Sort.Direction sortDirection = "DESC".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        direction = sortDirection.name();

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        Pageable unsortedPageable = PageRequest.of(page, size);

        Page<Fighter> fighterPage;

        switch (filterType) {
            case "name" -> {
                String term = (search != null && !search.isBlank()) ? search : "";
                fighterPage = fighterService.searchByName(term, pageable);
            }
            case "health" -> {
                int threshold = 1200;
                if (search != null && !search.isBlank()) {
                    try { threshold = Integer.parseInt(search.trim()); } catch (NumberFormatException ignored) {}
                }
                fighterPage = fighterService.getByHealthGreaterThan(threshold, pageable);
            }
            case "strongest" -> fighterPage = fighterService.getStrongestFighters(unsortedPageable);
            case "balanced"  -> fighterPage = fighterService.getBalancedFighters(1200, 50.0, unsortedPageable);
            default          -> fighterPage = fighterService.getFightersPaged(pageable);
        }

        model.addAttribute("fighters",      fighterPage.getContent());
        model.addAttribute("totalPages",    fighterPage.getTotalPages());
        model.addAttribute("totalElements", fighterPage.getTotalElements());
        model.addAttribute("currentPage",   page);
        model.addAttribute("pageSize",      size);
        model.addAttribute("hasPrevious",   fighterPage.hasPrevious());
        model.addAttribute("hasNext",       fighterPage.hasNext());
        model.addAttribute("search",        search);
        model.addAttribute("sort",          sort);
        model.addAttribute("direction",     direction);
        model.addAttribute("filterType",    filterType);

        return "Fighters";
    }

}
