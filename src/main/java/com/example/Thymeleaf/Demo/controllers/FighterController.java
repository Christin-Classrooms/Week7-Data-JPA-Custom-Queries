package com.example.Thymeleaf.Demo.controllers;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.Service.FighterService;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Controller
public class FighterController {

    private final FighterService fighterService;

    public FighterController(FighterService fighterService) {
        this.fighterService = fighterService;
    }

    @GetMapping("/fighters")
    public String getFighters(
        // pagination parameters
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,

        // sorting parameters
        @RequestParam(defaultValue = "id") String sort,
        @RequestParam(defaultValue = "ASC") String direction,

        // search/filter parameters
        @RequestParam(required = false) String search,
        @RequestParam(defaultValue = "all") String filterType,

        Model model) {
            Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

            Sort sortBy = Sort.by(sortDirection, sort);

            Pageable pageable = PageRequest.of(page, size, sortBy);
            
            // list of fighters on current page
            Page<Fighter> fighterPage;

            // filter type: name, health, strongest, balanced, default (findAll)
            if (search != null && !search.isEmpty()) {
                switch (filterType) {
                    case "name":
                        fighterPage = fighterService.findByNameContainingIgnoreCase(search, pageable);
                        break;
                    case "health":
                        try {
                            int healthValue = Integer.parseInt(search);
                            fighterPage = fighterService.findByHealthGreaterThan(healthValue, pageable);
                        } catch (NumberFormatException e) {
                            fighterPage = Page.empty(pageable);
                        }
                        break;
                    case "strongest":
                        fighterPage = fighterService.findStrongestFighters(pageable);
                        break;
                    case "balanced":
                        double minHealth = 1000;
                        double maxDamage = 100;

                        fighterPage = fighterService.findBalancedFighters(minHealth, maxDamage, pageable);
                        break;
                    default:
                        fighterPage = fighterService.getAllFightersPageable(pageable);
                        break;
                }
            } else {
                fighterPage = fighterService.getAllFightersPageable(pageable);
            }

            // attributes
            model.addAttribute("fighters", fighterPage.getContent());
            model.addAttribute("totalPages", fighterPage.getTotalPages());
            model.addAttribute("totalElements", fighterPage.getTotalElements());
            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);
            model.addAttribute("hasPrevious", fighterPage.hasPrevious());
            model.addAttribute("hasNext", fighterPage.hasNext());
            model.addAttribute("search", search);
            model.addAttribute("sort", sort);
            model.addAttribute("direction", direction);
            model.addAttribute("filterType", filterType);

            return "Fighters";
        }

        /*List<Fighter> fighters = fighterService.getAllFighters();

        model.addAttribute("fighters", fighters);
        model.addAttribute("total", fighters.size());
        return "Fighters";*/
}
