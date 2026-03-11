package com.example.Thymeleaf.Demo.controllers;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.repository.FighterRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FighterController {

    // Injecting the Repository directly to access the custom query methods easily
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
            @RequestParam(required = false) String filterType,
            Model model) {

        // 1. Determine Sort Direction
        Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        // 2. Create Pageable object with page, size, and sorting parameters
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        // 3. Initialize the Page object
        Page<Fighter> fighterPage;

        // 4. Handle Filter Logic using the custom methods from FighterRepository
        if (filterType != null && !filterType.isEmpty()) {
            switch (filterType.toLowerCase()) {
                case "name":
                    // Use search param to find by name
                    fighterPage = fighterRepository.findByNameContainingIgnoreCase(search != null ? search : "", pageable);
                    break;
                case "health":
                    // Parse the search param to an integer for health. Default to 0 if invalid.
                    int healthVal = 0;
                    try {
                        if (search != null && !search.isEmpty()) {
                            healthVal = Integer.parseInt(search);
                        }
                    } catch (NumberFormatException e) {
                        healthVal = 0;
                    }
                    fighterPage = fighterRepository.findByHealthGreaterThan(healthVal, pageable);
                    break;
                case "strongest":
                    fighterPage = fighterRepository.findStrongestFighters(pageable);
                    break;
                case "balanced":
                    // Providing default stats for a "balanced" fighter (e.g. min health 1000, max damage 50)
                    fighterPage = fighterRepository.findBalancedFighters(1000.0, 50.0, pageable);
                    break;
                default:
                    fighterPage = fighterRepository.findAll(pageable);
                    break;
            }
        } else {
            // Default: Show all fighters if no filter is applied
            fighterPage = fighterRepository.findAll(pageable);
        }

        // 5. Add attributes to the model for Thymeleaf to read
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
