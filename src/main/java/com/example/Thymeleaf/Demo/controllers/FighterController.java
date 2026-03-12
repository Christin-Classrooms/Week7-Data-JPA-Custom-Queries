package com.example.Thymeleaf.Demo.controllers;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.Service.FighterService;
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

    public FighterController(FighterService fighterService) {
        this.fighterService = fighterService;
    }

    @GetMapping("/fighters")
    public String getFighters(
        @RequestParam(name = "page", required = false, defaultValue = "0") int page,
        @RequestParam(name = "size", required = false, defaultValue = "10") int pageSize,
        @RequestParam(name = "sort", required = false, defaultValue = "id") String sortBy,
        @RequestParam(name = "direction", required = false, defaultValue = "ASC") String direction,
        @RequestParam(name = "search", required = false, defaultValue = "") String search,
        @RequestParam(name = "filterType", required = false, defaultValue = "all") String filterType,
        Model model) {

        Sort.Direction sortedDirection = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(sortedDirection, sortBy);
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<Fighter> fighterPage;

        switch (filterType) {
            case "name":
                fighterPage = fighterService.findFighterByName(search, pageable);
                break;
            case "health":
                int healthInt = (search != null && !search.isEmpty() ? Integer.parseInt(search) : 0);
                fighterPage = fighterService.findByHealth(healthInt, pageable);
                break;
            case "strongest":
                fighterPage = fighterService.findByStrongest(pageable);
                break;
            case "balanced":
                fighterPage = fighterService.findByBalanced(1000, 100, pageable);
                break;
            default:
                fighterPage = fighterService.getAllFightersPageable(pageable);
                break;
        }

        model.addAttribute("fighters", fighterPage.getContent());
        model.addAttribute("totalPages", fighterPage.getTotalPages());
        model.addAttribute("totalElements", fighterPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("hasPrevious", fighterPage.hasPrevious());
        model.addAttribute("hasNext", fighterPage.hasNext());
        model.addAttribute("search", search);
        model.addAttribute("sort", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("filterType", filterType);

        return "Fighters";
    }

}