package com.example.Thymeleaf.Demo.Service;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.repository.FighterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FighterService {

    @Autowired
    private FighterRepository fighterRepository;

    public void addFighter(Fighter fighter) {
        fighterRepository.save(fighter);
    }

    public Page<Fighter> getFighters(String filterType, String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            if ("health".equals(filterType)) {
                return fighterRepository.findByHealthGreaterThan(Integer.parseInt(search), pageable);
            }
            if ("name".equals(filterType)) {
                return fighterRepository.findByNameContainingIgnoreCase(search, pageable);
            }
        }

        return switch (filterType) {
            case "strongest" -> fighterRepository.findStrongestFighters(pageable);
            case "balanced" -> fighterRepository.findBalancedFighters(1100.0, 90.0, pageable);
            default -> fighterRepository.findAll(pageable);
        };
    }
}