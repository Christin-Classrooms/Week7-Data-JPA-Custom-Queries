package com.example.Thymeleaf.Demo.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.repository.FighterRepository;

@Controller
public class FighterController {

    private final FighterRepository fighterRepository;

    public FighterController(FighterRepository fighterRepository) {
        this.fighterRepository = fighterRepository;
    }

    @GetMapping("/fighters")
    public String getFighters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "all") String filterType,
            
            Model model) {
        
        Sort sortOrder = Sort.by(Sort.Direction.fromString(direction), sort);
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        
        Page<Fighter> fighterPage;
        
        if ("name".equals(filterType) && search != null && !search.isEmpty()) {
            fighterPage = fighterRepository.findByNameContainingIgnoreCase(search, pageable);
        } 
        else if ("health".equals(filterType) && search != null && !search.isEmpty()) {
            try {
                int health = Integer.parseInt(search);
                fighterPage = fighterRepository.findByHealthGreaterThan(health, pageable);
            } catch (NumberFormatException e) {
                fighterPage = fighterRepository.findAll(pageable);
            }
        }
        else if ("strongest".equals(filterType)) {
            fighterPage = fighterRepository.findStrongestFighters(pageable);
        }
        else if ("balanced".equals(filterType) && search != null && !search.isEmpty()) {
            try {
                double minHealth = Double.parseDouble(search);
                fighterPage = fighterRepository.findBalancedFighters(minHealth, 50.0, pageable);
            } catch (NumberFormatException e) {
                fighterPage = fighterRepository.findAll(pageable);
            }
        }
        else {
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