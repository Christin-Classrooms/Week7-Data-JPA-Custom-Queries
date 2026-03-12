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
        
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        Page<Fighter> fighters;
        
        
        if ("name".equals(filterType) && search != null && !search.isEmpty()) {
            fighters = fighterRepository.findByNameContainingIgnoreCase(search, pageable);
        } else if ("health".equals(filterType)) {
            fighters = fighterRepository.findByHealthGreaterThan(1200, pageable);
        } else if ("strongest".equals(filterType)) {
            fighters = fighterRepository.findStrongestFighters(pageable);
        } else if ("balanced".equals(filterType)) {
            fighters = fighterRepository.findBalancedFighters(1200, 80, pageable);
        } else {
            fighters = fighterRepository.findAll(pageable);
        }
        
       
        model.addAttribute("fighters", fighters.getContent());
        model.addAttribute("totalPages", fighters.getTotalPages());
        model.addAttribute("totalElements", fighters.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("hasPrevious", fighters.hasPrevious());
        model.addAttribute("hasNext", fighters.hasNext());
        model.addAttribute("search", search != null ? search : "");
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("filterType", filterType);
        
        return "Fighters";
    }

}