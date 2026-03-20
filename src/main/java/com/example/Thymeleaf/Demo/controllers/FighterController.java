package com.example.Thymeleaf.Demo.controllers;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.Service.FighterService;
import com.example.Thymeleaf.Demo.repository.FighterRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class FighterController {

    private final FighterService fighterService;
    private final FighterRepository fighterRepository;

    public FighterController(FighterService fighterService, FighterRepository fighterRepository) {
        this.fighterService = fighterService;
        this.fighterRepository = fighterRepository;
    }

    @GetMapping("/fighters")
    public String getFighters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String filterType,
            Model model) {

        Sort sortOrder = direction.equalsIgnoreCase("DESC") ? Sort.by(sort).descending() : Sort.by(sort).ascending();
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Page<Fighter> fighterPage;

        if (filterType != null && !filterType.isEmpty()) {
            switch (filterType) {
                case "name":
                    fighterPage = fighterRepository.findByNameContainingIgnoreCase(search != null ? search : "", pageable);
                    break;
                case "health":
                    fighterPage = fighterRepository.findByHealthGreaterThan(1200, pageable);
                    break;
                case "strongest":
                    fighterPage = fighterRepository.findStrongestFighters(pageable);
                    break;
                case "balanced":
                    fighterPage = fighterRepository.findBalancedFighters(1000, 80, pageable);
                    break;
                default:
                    fighterPage = fighterRepository.findAll(pageable);
            }
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

