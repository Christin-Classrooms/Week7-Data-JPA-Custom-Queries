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
import com.example.Thymeleaf.Demo.repository.FighterRepository;

@Controller
public class FighterController {

    private final FighterRepository fighterRepository;

    public FighterController(FighterRepository fighterRepository) {
        this.fighterRepository = fighterRepository;
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

        Sort sortOrder = Sort.by(Sort.Direction.fromString(direction), sort);
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Page<Fighter> fighterPage;

        switch (filterType) {
            case "name":
                if (search == null || search.trim().isEmpty()) {
                    fighterPage = fighterRepository.findAll(pageable);
                } else {
                    fighterPage = fighterRepository.findByNameContainingIgnoreCase(search, pageable);
                }
                break;
            case "health":
                int healthThreshold;
                try {
                    healthThreshold = Integer.parseInt(search);
                } catch (NumberFormatException e) {
                    healthThreshold = 0;
                }
                fighterPage = fighterRepository.findByHealthGreaterThan(healthThreshold, pageable);
                break;
            case "strongest":
                fighterPage = fighterRepository.findStrongestFighters(pageable);
                break;
            case "balanced":
                fighterPage = fighterRepository.findBalancedFighters(1000, 50, pageable);
                break;
            default:
                fighterPage = fighterRepository.findAll(pageable);
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
        model.addAttribute("filterType", filterType);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);

        return "Fighters";
    }
}