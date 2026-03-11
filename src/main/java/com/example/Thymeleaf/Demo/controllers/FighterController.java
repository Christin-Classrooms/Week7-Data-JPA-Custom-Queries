package com.example.Thymeleaf.Demo.controllers;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.Service.FighterService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
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
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sort,
        @RequestParam(defaultValue = "ASC") String direction,
        @RequestParam(required  = false) String search,
        @RequestParam(defaultValue = "all") String filterType,
        Model model) { 

            Sort sortOrder = direction.equalsIgnoreCase("DESC") ? Sort.by(sort).descending()
                : Sort.by(sort).ascending();

            Pageable pageable = PageRequest.of(page, size, sortOrder);

            Page<Fighter> fighterPage;

            if (search == null || search.isBlank()){
                search = "";
            }

        switch (filterType) {
            case "name":
                fighterPage = fighterService.findFightersByName(search, pageable);
                break;

            case "health":
                try {
                    int health = Integer.parseInt(search);
                    fighterPage = fighterService.findFightersByHealth(health, pageable);

                } catch (NumberFormatException e) {
                    fighterPage = fighterService.getAllFighters(pageable);
                    
                }
                break;

            case "strongest":
                fighterPage = fighterService.findStrongestFighters(pageable);
                break;

            case "balanced":
                double minHealth = 1000.0;
                double maxDamage = 500.0;
                fighterPage = fighterService.findBalancedFighters(minHealth, maxDamage, pageable);
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
        model.addAttribute("search",search);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("filterType", filterType);

        return "Fighters";

        }

}
