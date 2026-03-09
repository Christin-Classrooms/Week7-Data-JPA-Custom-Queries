package com.example.Thymeleaf.Demo.controllers;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.Service.FighterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.data.domain.*;

@Controller
public class FighterController {

    private final FighterService fighterService;

    public FighterController(FighterService fighterService) {
        this.fighterService = fighterService;
    }

    @GetMapping("/fighters")
    public String getFighters(
            @RequestParam(name = "search", defaultValue = "") String search,
            @RequestParam(name = "minHealth", defaultValue = "1000") int minHealth,
            @RequestParam(name = "maxDamage", defaultValue = "100") double maxDamage,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction,
            @RequestParam(name = "filterType", defaultValue = "all") String filterType,
            Model model) {

        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<Fighter> fighterPage;

        if (filterType.equalsIgnoreCase("name")) {
            fighterPage = fighterService.findByName(search, pageable);
        }
        else if (filterType.equalsIgnoreCase("health")) {
            fighterPage = fighterService.findByHealthGreaterThan(minHealth, pageable);
        }
        else if (filterType.equalsIgnoreCase("strongest")) {
            fighterPage = fighterService.findStrongestFighters(pageable);
        }
        else if (filterType.equalsIgnoreCase("balanced")) {
            fighterPage = fighterService.findBalancedFighters(minHealth, maxDamage, pageable);
        }
        else {
            fighterPage = fighterService.getAllFightersPageable(pageable);
        }

        model.addAttribute("fighters", fighterPage.getContent());
        model.addAttribute("total", fighterPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", fighterPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("search", search);
        model.addAttribute("minHealth", minHealth);
        model.addAttribute("maxDamage", maxDamage);
        model.addAttribute("sort", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("filterType", filterType);
        model.addAttribute("hasPrevious", fighterPage.hasPrevious());
        model.addAttribute("hasNext", fighterPage.hasNext());
        model.addAttribute("startIndex", page * size + 1);
        model.addAttribute("endIndex",
                Math.min((page + 1) * size, (int) fighterPage.getTotalElements()));

        return "Fighters";
    }
}