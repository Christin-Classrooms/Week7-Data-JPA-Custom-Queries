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
            @RequestParam(name = "page",       required = false, defaultValue = "0")   int page,
            @RequestParam(name = "size",       required = false, defaultValue = "10")  int size,
            @RequestParam(name = "sort",       required = false, defaultValue = "id")  String sortBy,
            @RequestParam(name = "direction",  required = false, defaultValue = "ASC") String direction,
            @RequestParam(name = "search",     required = false, defaultValue = "")    String search,
            @RequestParam(name = "filterType", required = false, defaultValue = "")    String filterType,
            Model model) {

        // Build Sort and Pageable
        Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC")
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // Choose the correct query based on filterType
        Page<Fighter> fighterPage;
        switch (filterType.toLowerCase()) {
            case "name":
                fighterPage = fighterService.findFighterByName(search, pageable);
                break;
            case "health":
                // Use search value as health threshold; fall back to 0 if blank/invalid
                int healthThreshold = 0;
                try { healthThreshold = Integer.parseInt(search); } catch (NumberFormatException ignored) {}
                fighterPage = fighterService.findFightersByHealthGreaterThan(healthThreshold, pageable);
                break;
            case "strongest":
                fighterPage = fighterService.findStrongestFighters(pageable);
                break;
            case "balanced":
                // Defaults: minHealth=1001, maxDamage=99.99
                fighterPage = fighterService.findBalancedFighters(1001, 99.99, pageable);
                break;
            default:
                fighterPage = fighterService.getAllFightersPageable(pageable);
                break;
        }

        // Model attributes
        model.addAttribute("fighters",      fighterPage.getContent());
        model.addAttribute("totalPages",    fighterPage.getTotalPages());
        model.addAttribute("totalElements", fighterPage.getTotalElements());
        model.addAttribute("currentPage",   page);
        model.addAttribute("pageSize",      size);
        model.addAttribute("hasPrevious",   fighterPage.hasPrevious());
        model.addAttribute("hasNext",       fighterPage.hasNext());
        model.addAttribute("search",        search);
        model.addAttribute("sort",          sortBy);
        model.addAttribute("direction",     direction);
        model.addAttribute("filterType",    filterType);

        return "Fighters";
    }

}
