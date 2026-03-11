package com.example.Thymeleaf.Demo.controllers;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.repository.FighterRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class FighterController {

    private final FighterRepository fighterRepository;

    public FighterController(FighterRepository fighterRepository) {
        this.fighterRepository = fighterRepository;
    }

    @GetMapping("/fighters")
    public String getFighters(
        Model model,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sort,
        @RequestParam(defaultValue = "ASC") String direction,
        @RequestParam(required = false) String search,
        @RequestParam(defaultValue = "all") String filterType) {

            Sort sortOrder = direction.equalsIgnoreCase("DESC")
            ? Sort.by(sort).descending()
            : Sort.by(sort).ascending();

            Pageable pageable = PageRequest.of(page, size, sortOrder);

            Page<Fighter> fighterPage;

            if (filterType.equals("name") && search != null && !search.isBlank()) {
                fighterPage = fighterRepository.findByNameContainingIgnoreCase(search, pageable);
            } else if (filterType.equals("health") && search != null && !search.isBlank()) {
                int healthValue = Integer.parseInt(search);
                fighterPage = fighterRepository.findByHealthGreaterThan(healthValue, pageable);
            } else if (filterType.equals("strongest")) {
                fighterPage = fighterRepository.findStrongestFighters(pageable);
            } else if (filterType.equals("balanced")) {
                fighterPage = fighterRepository.findBalancedFighters(1000, 100, pageable);
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
