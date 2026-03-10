package com.example.Thymeleaf.Demo.controllers;

import com.example.Thymeleaf.Demo.Model.Fighter;

import com.example.Thymeleaf.Demo.repository.FighterRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class FighterController {

    private final FighterRepository fighterRepository;

   public FighterController(FighterRepository fighterRepository) {
    this.fighterRepository = fighterRepository;

 @GetMapping("/fighters")
public String getFighters(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sort,
        @RequestParam(defaultValue = "ASC") String direction,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String filterType,
        Model model) {

    Sort sortOrder = direction.equalsIgnoreCase("ASC")
            ? Sort.by(sort).ascending()
            : Sort.by(sort).descending();

    Pageable pageable = PageRequest.of(page, size, sortOrder);

Page<Fighter> fighterPage;

if ("name".equalsIgnoreCase(filterType) && search != null && !search.isEmpty()) {
    fighterPage = fighterRepository.findByNameContainingIgnoreCase(search, pageable);
}
else if ("health".equalsIgnoreCase(filterType) && search != null && !search.isEmpty()) {
    int healthValue = Integer.parseInt(search);
    fighterPage = fighterRepository.findByHealthGreaterThan(healthValue, pageable);
}
else if ("strongest".equalsIgnoreCase(filterType)) {
    fighterPage = fighterRepository.findStrongestFighters(pageable);
}
else if ("balanced".equalsIgnoreCase(filterType)) {
    fighterPage = fighterRepository.findBalancedFighters(1000, 300, pageable);
}
else {
    fighterPage = fighterRepository.findAll(pageable);
}







}
}