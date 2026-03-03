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
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "ASC") String direction,
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "filterType", required = false, defaultValue = "all") String filterType,
            @RequestParam(name = "minHealth", required = false, defaultValue = "1200") int minHealth,
            @RequestParam(name = "maxDamage", required = false, defaultValue = "50") double maxDamage,
            Model model) {

        Sort.Direction sortedDirection = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(sortedDirection,sortBy);

        Pageable pageable = PageRequest.of(page,size,sort);

        // If the user typed something in the search box but left Filter Type as "All",
        // treat it as a name search so the Search button actually filters results.
        if (search != null && !search.trim().isEmpty()
                && (filterType == null || filterType.trim().isEmpty() || filterType.equalsIgnoreCase("all"))) {
            filterType = "name";
        }


        Page<Fighter> fighterPage;

        switch (filterType == null ? "all" : filterType.toLowerCase()) {
            case "name":
                fighterPage = fighterService.findFighterByName(search, pageable);
                break;
            case "health":
                fighterPage = fighterService.findFightersByHealthGreaterThan(minHealth, pageable);
                break;
                case "strongest":
                fighterPage = fighterService.findStrongestFighters(pageable);
                break;
            case "balanced":
                fighterPage = fighterService.findBalancedFighters(minHealth, maxDamage, pageable);
                break;
            default: fighterPage = fighterService.getAllFightersPageable(pageable);
        }

        model.addAttribute("fighters", fighterPage.getContent());
        model.addAttribute("totalElements", fighterPage.getTotalElements());
        model.addAttribute("totalPages", fighterPage.getTotalPages());
        model.addAttribute("currentPage",page);
        model.addAttribute("pageSize", size);
        model.addAttribute("hasPrevious", fighterPage.hasPrevious());
        model.addAttribute("hasNext", fighterPage.hasNext());
        model.addAttribute("search", search);
        model.addAttribute("sort", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("filterType", filterType);
        model.addAttribute("minHealth", minHealth);
        model.addAttribute("maxDamage", maxDamage);
        model.addAttribute("startIndex", page * size +1);
        model.addAttribute("endIndex", Math.min((page+1)*size,(int)fighterPage.getTotalElements()));

        return "Fighters";
    }

}
