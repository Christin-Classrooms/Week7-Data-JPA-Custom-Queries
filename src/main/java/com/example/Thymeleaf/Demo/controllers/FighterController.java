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

    private final FighterRepository fighterRepository;

    public FighterController(FighterRepository fighterRepository) {
        this.fighterRepository = fighterRepository;
    }

    @GetMapping("/fighters")
    public String getFighters(Model model,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sort,
        @RequestParam(defaultValue = "ASC") String direction,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String filterType

    ) {

        Sort.Direction dir = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));

        Page<Fighter> fighterPage;

        if ("name".equalsIgnoreCase(filterType) && search != null && !search.isEmpty()) {
            fighterPage = fighterRepository.findByNameContainingIgnoreCase(search, pageable);

        } else if ("health".equalsIgnoreCase(filterType) && !search.isEmpty()) {
            int health = Integer.parseInt(search);
            fighterPage = fighterRepository.findByHealthGreaterThan(health, pageable);

        } else if ("strongest".equalsIgnoreCase(filterType)) {
            fighterPage = fighterRepository.findStrongestFightersDesc(pageable);

        } else if ("balanced".equalsIgnoreCase(filterType)) {
            fighterPage = fighterRepository.findBalancedFightersDesc(1200, 60, pageable);

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

        model.addAttribute("startIndex", page * size +1);
        model.addAttribute("endIndex", Math.min((page + 1) * size, (int) fighterPage.getTotalElements()));

        return "Fighters";
    }

}
