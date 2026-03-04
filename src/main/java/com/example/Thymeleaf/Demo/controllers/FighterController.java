package com.example.Thymeleaf.Demo.controllers;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.repository.FighterRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FighterController {

    private final FighterRepository fighterRepository;

    public FighterController(FighterRepository fighterRepository) {
        this.fighterRepository = fighterRepository;
    }

    @GetMapping("/fighters")
    public String getFighters(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "id") String sort,
            @RequestParam(name = "direction", required = false, defaultValue = "ASC") String direction,
            @RequestParam(name = "filterType", required = false, defaultValue = "all") String filterType,
            Model model) {

        Page<Fighter> fighterPage;

        switch (filterType.toLowerCase()) {

            case "name":
                Sort nameSort = direction.equalsIgnoreCase("DESC") ? Sort.by("name").descending() : Sort.by("name").ascending();
                Pageable namePageable = PageRequest.of(page, size, nameSort);
                if (!search.trim().isEmpty()) {
                    fighterPage = fighterRepository.findByNameContainingIgnoreCase(search.trim(), namePageable);
                } else {
                    fighterPage = fighterRepository.findAll(namePageable);
                }
                break;

            case "health":
                // Always sort by health DESC regardless of request sort
                Sort healthSort = Sort.by("health").descending();
                Pageable healthPageable = PageRequest.of(page, size, healthSort);
                fighterPage = fighterRepository.findByHealthGreaterThan(1200, healthPageable);
                break;

            case "strongest":
                // Sort strongest by damage DESC
                Sort damageSort = Sort.by("damage").descending();
                Pageable strongestPageable = PageRequest.of(page, size, damageSort);
                fighterPage = fighterRepository.findStrongestFighters(strongestPageable);
                break;

            case "balanced":
                // Balanced: sort by resistance DESC
                Sort balancedSort = Sort.by("resistance").descending();
                Pageable balancedPageable = PageRequest.of(page, size, balancedSort);
                fighterPage = fighterRepository.findBalancedFighters(1200, 80, balancedPageable);
                break;

            default:
                // Default: sort by whatever the user chose
                Sort defaultSort = direction.equalsIgnoreCase("DESC") ? Sort.by(sort).descending() : Sort.by(sort).ascending();
                Pageable defaultPageable = PageRequest.of(page, size, defaultSort);
                if (!search.trim().isEmpty()) {
                    fighterPage = fighterRepository.findByNameContainingIgnoreCase(search.trim(), defaultPageable);
                } else {
                    fighterPage = fighterRepository.findAll(defaultPageable);
                }
        }

        // Send data to Thymeleaf
        model.addAttribute("fighters", fighterPage.getContent());
        model.addAttribute("totalElements", fighterPage.getTotalElements());
        model.addAttribute("totalPages", fighterPage.getTotalPages());
        model.addAttribute("currentPage", fighterPage.getNumber());
        model.addAttribute("pageSize", fighterPage.getSize());
        model.addAttribute("hasPrevious", fighterPage.hasPrevious());
        model.addAttribute("hasNext", fighterPage.hasNext());

        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("filterType", filterType);

        return "Fighters";
    }
}