package com.example.Thymeleaf.Demo.controllers;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.repository.FighterRepository;
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

    private final FighterRepository fighterRepository;

    public FighterController(FighterRepository fighterRepository) {
        this.fighterRepository = fighterRepository;
    }

    @GetMapping("/fighters")
    public String getFighters(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "all") String filterType
    ) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<Fighter> fighterPage;

        if (filterType.equalsIgnoreCase("name") && search != null && !search.trim().isEmpty()) {
            fighterPage = fighterRepository.findByNameContainingIgnoreCase(search, pageable);
        } else if (filterType.equalsIgnoreCase("health")) {
            fighterPage = fighterRepository.findByHealthGreaterThan(1200, pageable);
        } else if (filterType.equalsIgnoreCase("strongest")) {
            fighterPage = fighterRepository.findStrongestFighters(pageable);
        } else if (filterType.equalsIgnoreCase("balanced")) {
            fighterPage = fighterRepository.findBalancedFighters(1200, 50, pageable);
        } else {
            fighterPage = fighterRepository.findAll(pageable);
        }

        model.addAttribute("fighters", fighterPage.getContent());
        model.addAttribute("totalPages", fighterPage.getTotalPages());
        model.addAttribute("totalElements", fighterPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("hasPrevious", fighterPage.hasPrevious());
        model.addAttribute("hasNext", fighterPage.hasNext());

        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("filterType", filterType);

        return "Fighters";
    }
}