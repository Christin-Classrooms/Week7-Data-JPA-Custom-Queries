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
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction,
            @RequestParam(name = "search", defaultValue = "") String search,
            @RequestParam(name = "filterType", defaultValue = "") String filterType,
            Model model) {
        
        // Validate sort field against allowed columns
        String[] allowedSortFields = {"id", "name", "health", "damage", "resistance"};
        boolean isValidSort = false;
        for (String field : allowedSortFields) {
            if (field.equals(sort)) {
                isValidSort = true;
                break;
            }
        }
        if (!isValidSort) {
            sort = "id";
        }
        
        Sort sortObj = Sort.by(Sort.Direction.fromString(direction), sort);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        
        // Choose repository method based on filterType
        Page<Fighter> fighterPage;
        if (!search.isEmpty() && ("name".equals(filterType) || filterType.isEmpty())) {
            fighterPage = fighterRepository.findByNameContainingIgnoreCase(search, pageable);
        } else if ("health".equals(filterType)) {
            fighterPage = fighterRepository.findByHealthGreaterThan(1200, pageable);
        } else if ("strongest".equals(filterType)) {
            fighterPage = fighterRepository.findStrongestFighters(pageable);
        } else if ("balanced".equals(filterType)) {
            fighterPage = fighterRepository.findBalancedFighters(1200, 150, pageable);
        } else {
            fighterPage = fighterRepository.findAll(pageable);
        }
        
        // Add attributes to model
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