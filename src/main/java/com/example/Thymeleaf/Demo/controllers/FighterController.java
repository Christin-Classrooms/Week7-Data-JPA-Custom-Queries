package com.example.Thymeleaf.Demo.controllers;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.Service.FighterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FighterController {

    @Autowired
    private FighterService fighterService;

    @GetMapping("/fighters")
    public String listFighters(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String filterType) {

        Sort.Direction dir = direction.equalsIgnoreCase("ASC")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));

        Page<Fighter> fighterPage;

        if ("name".equals(filterType) && search != null && !search.isEmpty()) {

            fighterPage = fighterService.findByNameContainingIgnoreCase(search, pageable);

        } else if ("health".equals(filterType) && search != null && !search.isEmpty()) {

            int health = Integer.parseInt(search);
            fighterPage = fighterService.findByHealthGreaterThan(health, pageable);

        } else if ("strongest".equals(filterType)) {

            fighterPage = fighterService.findStrongestFighters(pageable);

        } else if ("balanced".equals(filterType)) {

            fighterPage = fighterService.findBalancedFighters(1200, 300, pageable);

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

        return "fighters";
    }
}