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
            @RequestParam(required = false, defaultValue = "all") String filterType,
            Model model) {

        List<String> allowedSortFields = List.of("id", "name", "health", "damage", "resistance");
        if (!allowedSortFields.contains(sort)) {
            sort = "id";
        }

        Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sorting;
        if ("name".equals(sort)) {
            sorting = Sort.by(new Sort.Order(sortDirection, "name").ignoreCase());
        } else {
            sorting = Sort.by(sortDirection, sort);
        }

        Pageable pageable = PageRequest.of(page, size, sorting);

        Page<Fighter> fighterPage;

        switch (filterType.toLowerCase()) {
            case "name":
                fighterPage = fighterRepository.findByNameContainingIgnoreCase(
                        search == null ? "" : search,
                        pageable
                );
                break;
            case "health":
                fighterPage = fighterRepository.findByHealthGreaterThan(1200, pageable);
                break;
            case "strongest":
                fighterPage = fighterRepository.findStrongestFighters(pageable);
                break;
            case "balanced":
                fighterPage = fighterRepository.findBalancedFighters(1200, 50.0, pageable);
                break;
            default:
                fighterPage = fighterRepository.findAll(pageable);
                break;
        }

        model.addAttribute("fighters", fighterPage.getContent());
        model.addAttribute("totalPages", fighterPage.getTotalPages());
        model.addAttribute("totalElements", fighterPage.getTotalElements());
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
