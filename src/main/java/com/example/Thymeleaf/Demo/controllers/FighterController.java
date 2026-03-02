package com.example.Thymeleaf.Demo.controllers;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.repository.FighterRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class FighterController {

    private final FighterRepository fighterRepository;

    public FighterController(FighterRepository fighterRepository) {
        this.fighterRepository = fighterRepository;
    }

    // ✅ LIST + PAGINATION + SORTING + FILTERS
    @GetMapping("/fighters")
    public String getFighters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction,

            // filter controls
            @RequestParam(required = false) String filterType,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer minHealth,
            @RequestParam(required = false) Double minHealthBalanced,
            @RequestParam(required = false) Double maxDamageBalanced,

            Model model
    ) {

        Sort sortObj = direction.equalsIgnoreCase("desc")
                ? Sort.by(sort).descending()
                : Sort.by(sort).ascending();

        Pageable pageable = PageRequest.of(page, size, sortObj);

        Page<Fighter> fighterPage;

        if ("name".equalsIgnoreCase(filterType) && name != null && !name.isBlank()) {
            fighterPage = fighterRepository.findByNameContainingIgnoreCase(name, pageable);

        } else if ("health".equalsIgnoreCase(filterType) && minHealth != null) {
            fighterPage = fighterRepository.findByHealthGreaterThan(minHealth, pageable);

        } else if ("strongest".equalsIgnoreCase(filterType)) {
            fighterPage = fighterRepository.findStrongestFighters(pageable);

        } else if ("balanced".equalsIgnoreCase(filterType)
                && minHealthBalanced != null && maxDamageBalanced != null) {
            fighterPage = fighterRepository.findBalancedFighters(minHealthBalanced, maxDamageBalanced, pageable);

        } else {
            fighterPage = fighterRepository.findAll(pageable);
        }

        // For Thymeleaf table
        model.addAttribute("fighters", fighterPage.getContent());

        // For pagination UI
        model.addAttribute("currentPage", fighterPage.getNumber());
        model.addAttribute("totalPages", fighterPage.getTotalPages());
        model.addAttribute("totalItems", fighterPage.getTotalElements());

        // Keep query params so pagination links can preserve them
        model.addAttribute("size", size);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);

        model.addAttribute("filterType", filterType);
        model.addAttribute("name", name);
        model.addAttribute("minHealth", minHealth);
        model.addAttribute("minHealthBalanced", minHealthBalanced);
        model.addAttribute("maxDamageBalanced", maxDamageBalanced);

        return "Fighters"; // Fighters.html
    }

    // ✅ SHOW CREATE FORM
    @GetMapping("/fighters/create")
    public String showCreateFighterForm(Model model) {
        model.addAttribute("fighter", new Fighter());
        return "CreateFighter"; // CreateFighter.html
    }

    // ✅ SAVE (CREATE/UPDATE)
    @PostMapping("/fighters/save")
    public String saveFighter(@ModelAttribute Fighter fighter) {
        fighterRepository.save(fighter);
        return "redirect:/fighters";
    }

    // ✅ DELETE by id
    @GetMapping("/fighters/delete")
    public String deleteFighter(@RequestParam("id") Integer id) {
        fighterRepository.deleteById(id);
        return "redirect:/fighters";
    }
}