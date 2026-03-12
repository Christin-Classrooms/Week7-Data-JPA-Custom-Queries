package com.example.Thymeleaf.Demo.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.repository.FighterRepository;

@Service
public class FighterService {

    private final FighterRepository fighterRepository;

    public FighterService(FighterRepository fighterRepository) {
        this.fighterRepository = fighterRepository;
    }

    public Fighter addFighter(Fighter fighter) {
        return fighterRepository.save(fighter);
    }

    // Get all fighters (used in older controller)
    public List<Fighter> getAllFighters() {
        return fighterRepository.findAll();
    }

    // Pagination version of getting all fighters
    public Page<Fighter> getAllFightersPageable(Pageable pageable) {
        return fighterRepository.findAll(pageable);
    }

    // Search fighters by name (case insensitive)
    public Page<Fighter> findByName(String name, Pageable pageable) {
        return fighterRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    // Fighters with health greater than a given value
    public Page<Fighter> findByHealthGreaterThan(int health, Pageable pageable) {
        return fighterRepository.findByHealthGreaterThan(health, pageable);
    }

    // Strongest fighters ordered by damage
    public Page<Fighter> findStrongest(Pageable pageable) {
        return fighterRepository.findStrongestFighters(pageable);
    }

    // Balanced fighters
    public Page<Fighter> findBalanced(double minHealth, double maxDamage, Pageable pageable) {
        return fighterRepository.findBalancedFighters(minHealth, maxDamage, pageable);
    }

}
