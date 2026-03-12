package com.example.Thymeleaf.Demo.controllers;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.repository.FighterRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Controller
public class FighterController {

    private final FighterRepository fighterRepository;

    public FighterController(FighterRepository fighterRepository) {
        this.fighterRepository = fighterRepository;
    }

    @GetMapping("/fighters")
    public String getFighters(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "ASC") String direction,
            @RequestParam(name = "filterType", required = false, defaultValue = "") String filterType,
            Model model) {

        if (!sortBy.equals("id") &&
            !sortBy.equals("name") &&
            !sortBy.equals("health") &&
            !sortBy.equals("damage") &&
            !sortBy.equals("resistance")) {
            sortBy = "id";
        }

        Sort.Direction sortedDirection = direction.equalsIgnoreCase("DESC")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sort = Sort.by(sortedDirection, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Fighter> fighterPage;

        if ("name".equalsIgnoreCase(filterType) && search != null && !search.isEmpty()) {
            fighterPage = fighterRepository.findByNameContainingIgnoreCase(search, pageable);

        } else if ("health".equalsIgnoreCase(filterType) && search != null && !search.isEmpty()) {
            try {
                int health = Integer.parseInt(search);
                fighterPage = fighterRepository.findByHealthGreaterThan(health, pageable);
            } catch (NumberFormatException e) {
                fighterPage = fighterRepository.findAll(pageable);
            }

        } else if ("strongest".equalsIgnoreCase(filterType)) {
            fighterPage = fighterRepository.findStrongestFighters(pageable);

        } else if ("balanced".equalsIgnoreCase(filterType)) {
            fighterPage = fighterRepository.findBalancedFighters(1000, 100, pageable);

        } else {
            fighterPage = fighterRepository.findAll(pageable);
        }

        model.addAttribute("fighters", fighterPage.getContent());
        model.addAttribute("pageSize", size);
        model.addAttribute("totalElements", fighterPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", fighterPage.getTotalPages());
        model.addAttribute("search", search);
        model.addAttribute("sort", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("filterType", filterType);
        model.addAttribute("hasPrevious", fighterPage.hasPrevious());
        model.addAttribute("hasNext", fighterPage.hasNext());
       

        return "fighters";
    }
}