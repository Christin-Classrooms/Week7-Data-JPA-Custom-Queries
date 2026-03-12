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
    public String listFighters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "all") String filterType,
            Model model) {

        Sort sortOrder = direction.equalsIgnoreCase("ASC")
                ? Sort.by(sort).ascending()
                : Sort.by(sort).descending();

        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<Fighter> fighterPage;

        switch (filterType) {
            case "name":
                fighterPage = fighterService.searchByName(search != null ? search : "", pageable);
                break;

            case "health":
                int healthThreshold = 1200;
                fighterPage = fighterService.getHighHealthFighters(healthThreshold, pageable);
                break;

            case "strongest":
                fighterPage = fighterService.getStrongestFighters(pageable);
                break;

            case "balanced":
                fighterPage = fighterService.getBalancedFighters(1200.0, 80.0, pageable);
                break;

            default:
                fighterPage = fighterService.getAllFighters(pageable);
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
