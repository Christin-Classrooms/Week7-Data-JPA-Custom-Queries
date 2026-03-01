// Name: Fahad Arif (N01729165)
// Course: Web Application Development (CPAN-228)

package com.example.Thymeleaf.Demo.controllers;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.Service.FighterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FighterController {

    private final FighterService fighterService;

    public FighterController(FighterService fighterService) {
        this.fighterService = fighterService;
    }

    @GetMapping("/fighters")
    public String getFighters(
            Model model,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction,

            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "all") String filterType,

            @RequestParam(defaultValue = "0") int healthMin,
            @RequestParam(defaultValue = "0") double minHealth,
            @RequestParam(defaultValue = "999999") double maxDamage
    ) {
        List<Fighter> fighters = fighterService.getAllFighters();

        List<Fighter> filtered = fighters;

        switch (filterType.toLowerCase()) {
            case "name":
                if (search != null && !search.trim().isEmpty()) {
                    String s = search.trim().toLowerCase();
                    filtered = filtered.stream().filter(f -> f.getName() != null && f.getName().toLowerCase().contains(s)).collect(Collectors.toList());
                }
                break;

            case "health":
                filtered = filtered.stream()
                        .filter(f -> f.getHealth() != null && f.getHealth() > healthMin)
                        .collect(Collectors.toList());
                break;

            case "strongest":
                filtered = filtered.stream().sorted(Comparator.comparing(Fighter::getDamage, Comparator.nullsLast(Comparator.naturalOrder())).reversed()).collect(Collectors.toList());
                sort = "id";
                direction = "ASC";
                break;

            case "balanced":
                filtered = filtered.stream().filter(f -> f.getHealth() != null && f.getDamage() != null && f.getResistance() != null).filter(f -> f.getHealth() >= minHealth && f.getDamage() <= maxDamage).sorted(Comparator.comparing(Fighter::getResistance, Comparator.nullsLast(Comparator.naturalOrder())).reversed()).collect(Collectors.toList());
                sort = "id";
                direction = "ASC";
                break;

            default:
                break;
        }

        Comparator<Fighter> comparator;
        switch (sort) {
            case "name":
                comparator = Comparator.comparing(Fighter::getName, Comparator.nullsLast(String::compareToIgnoreCase));
                break;
            case "health":
                comparator = Comparator.comparing(Fighter::getHealth, Comparator.nullsLast(Integer::compareTo));
                break;
            case "damage":
                comparator = Comparator.comparing(Fighter::getDamage, Comparator.nullsLast(Double::compareTo));
                break;
            case "resistance":
                comparator = Comparator.comparing(Fighter::getResistance, Comparator.nullsLast(Double::compareTo));
                break;
            case "id":
            default:
                comparator = Comparator.comparingInt(Fighter::getId);
                sort = "id";
                break;
        }

        if ("DESC".equalsIgnoreCase(direction)) {
            comparator = comparator.reversed();
            direction = "DESC";
        } else {
            direction = "ASC";
        }

        filtered = filtered.stream().sorted(comparator).collect(Collectors.toList());

        int totalElements = filtered.size();
        int totalPages = (int) Math.ceil(totalElements / (double) size);

        if (totalPages == 0) {
            page = 0;
        } else if (page < 0) {
            page = 0;
        } else if (page > totalPages - 1) {
            page = totalPages - 1;
        }

        int start = page * size;
        int end = Math.min(start + size, totalElements);

        List<Fighter> pageContent = (start < end) ? filtered.subList(start, end) : List.of();

        boolean hasPrevious = page > 0;
        boolean hasNext = page < totalPages - 1;

        model.addAttribute("fighters", pageContent);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("hasPrevious", hasPrevious);
        model.addAttribute("hasNext", hasNext);

        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("filterType", filterType);

        model.addAttribute("healthMin", healthMin);
        model.addAttribute("minHealth", minHealth);
        model.addAttribute("maxDamage", maxDamage);

        return "Fighters";
    }
}