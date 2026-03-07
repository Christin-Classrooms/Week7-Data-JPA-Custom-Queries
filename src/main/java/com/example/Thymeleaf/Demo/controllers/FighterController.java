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
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "all") String filterType
    ) {

        Sort sorting = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sort).descending()
                : Sort.by(sort).ascending();

        Pageable pageable = PageRequest.of(page, size, sorting);

        Page<Fighter> fighterPage;

        if ("name".equals(filterType) && search != null && !search.isBlank()) {
            fighterPage = fighterService.findByName(search, pageable);
        } else if ("health".equals(filterType) && search != null && !search.isBlank()) {
            int healthValue = Integer.parseInt(search);
            fighterPage = fighterService.findByHealth(healthValue, pageable);
        } else if ("strongest".equals(filterType)) {
            fighterPage = fighterService.findStrongest(pageable);
        } else if ("balanced".equals(filterType)) {
            fighterPage = fighterService.findBalanced(1200, 80, pageable);
        } else {
            fighterPage = fighterService.findAll(pageable);
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
