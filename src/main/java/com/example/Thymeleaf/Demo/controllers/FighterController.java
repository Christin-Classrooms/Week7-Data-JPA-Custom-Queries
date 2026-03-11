package com.example.Thymeleaf.Demo.controllers;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.repository.FighterRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/fighters")
public class FighterController {

    private final FighterRepository fighterRepository;

    public FighterController(FighterRepository fighterRepository) {
        this.fighterRepository = fighterRepository;
    }

    // ── LIST (with pagination, sorting, search/filter) ─────────────────────────

    @GetMapping
    public String listFighters(
            @RequestParam(defaultValue = "0")    int    page,
            @RequestParam(defaultValue = "10")   int    size,
            @RequestParam(defaultValue = "id")   String sort,
            @RequestParam(defaultValue = "ASC")  String direction,
            @RequestParam(required = false)      String search,
            @RequestParam(defaultValue = "all")  String filterType,
            Model model) {

        // Build Sort direction
        Sort.Direction sortDir = direction.equalsIgnoreCase("DESC")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        // Validate sort field to prevent injection
        List<String> allowedSortFields = List.of("id", "name", "health", "damage", "resistance");
        if (!allowedSortFields.contains(sort)) {
            sort = "id";
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sort));

        // Choose which query to run based on filterType
        Page<Fighter> fighterPage;

        switch (filterType) {
            case "name" -> {
                String term = (search != null && !search.isBlank()) ? search : "";
                fighterPage = fighterRepository.findByNameContainingIgnoreCase(term, pageable);
            }
            case "health" -> {
                // Filter fighters with health > 1200
                fighterPage = fighterRepository.findByHealthGreaterThan(1200, pageable);
            }
            case "strongest" -> {
                fighterPage = fighterRepository.findStrongestFighters(pageable);
            }
            case "balanced" -> {
                // minHealth = 1200, maxDamage = 80 (sensible defaults for "balanced")
                fighterPage = fighterRepository.findBalancedFighters(1200, 80, pageable);
            }
            default -> {
                fighterPage = fighterRepository.findAll(pageable);
            }
        }

        // ── Model attributes ──────────────────────────────────────────────────
        model.addAttribute("fighters",       fighterPage.getContent());
        model.addAttribute("totalPages",     fighterPage.getTotalPages());
        model.addAttribute("totalElements",  fighterPage.getTotalElements());
        model.addAttribute("currentPage",    page);
        model.addAttribute("pageSize",       size);
        model.addAttribute("hasPrevious",    fighterPage.hasPrevious());
        model.addAttribute("hasNext",        fighterPage.hasNext());
        model.addAttribute("search",         search);
        model.addAttribute("sort",           sort);
        model.addAttribute("direction",      direction);
        model.addAttribute("filterType",     filterType);

        return "Fighters";
    }

    // ── CREATE ─────────────────────────────────────────────────────────────────

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("fighter", new Fighter());
        return "CreateFighter";
    }

    @PostMapping
    public String createFighter(@Valid @ModelAttribute("fighter") Fighter fighter,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "CreateFighter";
        }
        fighterRepository.save(fighter);
        return "redirect:/fighters";
    }

    // ── EDIT ───────────────────────────────────────────────────────────────────

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable int id, Model model) {
        Fighter fighter = fighterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid fighter id: " + id));
        model.addAttribute("fighter", fighter);
        return "CreateFighter";
    }

    @PostMapping("/{id}/edit")
    public String updateFighter(@PathVariable int id,
                                @RequestParam String name,
                                @RequestParam Integer health,
                                @RequestParam Double damage,
                                @RequestParam Double resistance,
                                Model model) {
        fighterRepository.updateFighter(id, name, health, damage, resistance);
        return "redirect:/fighters";
    }

    // ── DELETE ─────────────────────────────────────────────────────────────────

    @PostMapping("/{id}/delete")
    public String deleteFighter(@PathVariable int id) {
        fighterRepository.deleteById(id);
        return "redirect:/fighters";
    }
}