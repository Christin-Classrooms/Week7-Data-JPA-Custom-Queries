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
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "ASC") String direction,
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "filterType", required = false, defaultValue = "all") String filterType,
            Model model) {

        Sort.Direction sortedDirection = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(sortedDirection, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Fighter> fighterPage;

        if (filterType.equalsIgnoreCase("name") && search != null && !search.isEmpty()) {
            fighterPage = fighterService.findFighterByName(search, pageable);

        } else if (filterType.equalsIgnoreCase("health") && search != null && !search.isEmpty()) {
            try {
                int healthValue = Integer.parseInt(search);
                fighterPage = fighterService.findFightersByHealthGreaterThan(healthValue, pageable);
            } catch (NumberFormatException e) {
                fighterPage = fighterService.getAllFightersPageable(pageable);
            }

        } else if (filterType.equalsIgnoreCase("strongest")) {
            fighterPage = fighterService.findStrongestFighters(pageable);

        } else if (filterType.equalsIgnoreCase("balanced")) {
            fighterPage = fighterService.findBalancedFighters(1000, 800, pageable);

        } else {
            if (search != null && !search.isEmpty()) {
                fighterPage = fighterService.findFighterByName(search, pageable);
            } else {
                fighterPage = fighterService.getAllFightersPageable(pageable);
            }
        }

        model.addAttribute("fighters", fighterPage.getContent());
        model.addAttribute("totalPages", fighterPage.getTotalPages());
        model.addAttribute("totalElements", fighterPage.getTotalElements());
        model.addAttribute("currentPage", fighterPage.getNumber());
        model.addAttribute("pageSize", fighterPage.getSize());
        model.addAttribute("hasPrevious", fighterPage.hasPrevious());
        model.addAttribute("hasNext", fighterPage.hasNext());
        model.addAttribute("search", search);
        model.addAttribute("sort", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("filterType", filterType);
        model.addAttribute("startIndex", page * size + 1);
        model.addAttribute("endIndex", Math.min((page + 1) * size, (int) fighterPage.getTotalElements()));

        return "Fighters";
    }
}