package com.example.Thymeleaf.Demo.controllers;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.Service.FighterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FighterController {

    private final FighterService fighterService;

    public FighterController(FighterService fighterService) {
        this.fighterService = fighterService;
    }

    @GetMapping("/fighters")
    public String getFighters(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String filterType
    ) {

        Sort.Direction dir = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));

        Page<Fighter> fighterPage;

        if ("name".equalsIgnoreCase(filterType) && search != null && !search.isEmpty()) {
            fighterPage = fighterService.findByName(search, pageable);

        } else if ("health".equalsIgnoreCase(filterType) && search != null && !search.isEmpty()) {
            try {
                int healthValue = Integer.parseInt(search);
                fighterPage = fighterService.findByHealthGreaterThan(healthValue, pageable);
            } catch (NumberFormatException e) {
                fighterPage = fighterService.findAll(pageable);
            }

        } else if ("strongest".equalsIgnoreCase(filterType)) {
            fighterPage = fighterService.findStrongest(pageable);

        } else if ("balanced".equalsIgnoreCase(filterType)) {
            // Pass some reasonable default min/max values to match your Service method
            fighterPage = fighterService.findBalanced(1000, 150, pageable);

        } else {
            fighterPage = fighterService.findAll(pageable);
        }

        // Add pagination & filter info to the model
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