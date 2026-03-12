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
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "all") String filterType,
            Model model) {

        if (sort == null || sort.equals("id")) {
            if ("strongest".equals(filterType)) {
                sort = "damage";
                direction = "DESC";
            } else if ("balanced".equals(filterType)) {
                sort = "resistance";
                direction = "DESC";
            } else {
                sort = "id";
            }
        }

        Sort sorting = direction.equalsIgnoreCase("DESC") ? Sort.by(sort).descending() : Sort.by(sort).ascending();
        Pageable pageable = PageRequest.of(page, size, sorting);
        Page<Fighter> fighterPage;

        if ("health".equals(filterType)) {
            fighterPage = fighterRepository.findByHealthGreaterThan(1200, pageable);
        } else if ("strongest".equals(filterType)) {
            fighterPage = fighterRepository.findStrongestFighters(pageable);
        } else if ("balanced".equals(filterType)) {
            fighterPage = fighterRepository.findBalancedFighters(1200.0, 90.0, pageable);
        } else if (search != null && !search.trim().isEmpty()) {
            fighterPage = fighterRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            fighterPage = fighterRepository.findAll(pageable);
        }

        model.addAttribute("fighters", fighterPage.getContent());
        model.addAttribute("totalPages", fighterPage.getTotalPages());
        model.addAttribute("totalElements", fighterPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("search", search);
        model.addAttribute("filterType", filterType);
        model.addAttribute("hasNext", fighterPage.hasNext());
        model.addAttribute("hasPrevious", fighterPage.hasPrevious());

        return "Fighters";
    }
}