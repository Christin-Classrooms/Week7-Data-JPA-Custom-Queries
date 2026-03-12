package com.example.Thymeleaf.Demo.controllers;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.Service.FighterService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FighterController {

    private final FighterService fighterService;

    public FighterController(FighterService fighterService) {
        this.fighterService = fighterService;
    }

    @GetMapping("/fighters")
    public String getFighters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") String filterType,
            Model model) {

        Page<Fighter> fighterPage = fighterService.getFightersPaginated(page, size, search, filterType);

        model.addAttribute("fighters", fighterPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", fighterPage.getTotalPages());
        model.addAttribute("totalItems", fighterPage.getTotalElements());
        model.addAttribute("search", search);
        model.addAttribute("filterType", filterType);

        return "Fighters";
    }
}