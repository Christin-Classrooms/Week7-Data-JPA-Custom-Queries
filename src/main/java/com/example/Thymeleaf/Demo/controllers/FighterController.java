package com.example.Thymeleaf.Demo.controllers;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.Service.FighterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
        @RequestParam(name = "page", required = false, defaultValue = "0") int page,
        @RequestParam(name = "size", required = false, defaultValue = "10") int size,
        @RequestParam(name = "sort", required = false, defaultValue = "id") String sortBy,
        @RequestParam(name = "direction", required = false, defaultValue = "ASC") String direction,
        Model model) {

        Sort.Direction sortedDirection = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(sortedDirection, sortBy);
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Fighter> fighterPage;

        if ("name".equalsIgnoreCase(sortBy) && search != null && !search.isEmpty()) {
            fighterPage = fighterService.findByNameContainingIgnoreCase(search, pageable);
        } else if ("health".equalsIgnoreCase(sortBy) && search != null && !search.isEmpty()) {
            try {
                int healthValue = Integer.parseInt(search);
                fighterPage = fighterService.findByHealthGreaterThan(healthValue, pageable);
            } catch (NumberFormatException e) {
                fighterPage = fighterService.getAllFighters(pageable);
            }
        } else if ("strongest".equalsIgnoreCase(sortBy)) {
            fighterPage = fighterService.findStrongestFighters(pageable);

        } else if("balance".equalsIgnoreCase(sortBy)) {
            fighterPage = fighterService.findBalancedFighters(1000,75.0, pageable);        
        } else if (search != null && !search.isEmpty()) {
            fighterPage = fighterService.findByNameContainingIgnoreCase(search, pageable);
        } else {
            fighterPage = fighterService.getAllFighters(pageable);
        }

        model.addAttribute("fighters", fighterPage.getContent());
        model.addAttribute("total", fighterPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", fighterPage.getTotalPages());
        model.addAttribute("search", search);
        model.addAttribute("sort", sortBy);
        model.addAttribute("direction", direction);

        model.addAttribute("hasPrevious", fighterPage.hasPrevious());
        model.addAttribute("hasNext", fighterPage.hasNext());
        return "Fighters";
    }

}
