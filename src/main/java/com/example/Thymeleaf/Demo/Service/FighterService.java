package com.example.Thymeleaf.Demo.Service;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.repository.FighterRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FighterService {

    private final FighterRepository fighterRepository;

    public FighterService(FighterRepository fighterRepository) {
        this.fighterRepository = fighterRepository;
    }

    // Search by name (case-insensitive, contains)
    public Page<Fighter> findByName(String name, Pageable pageable) {
        return fighterRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    // Search by health greater than
    public Page<Fighter> findByHealthGreaterThan(int health, Pageable pageable) {
        return fighterRepository.findByHealthGreaterThan(health, pageable);
    }

    // Get strongest fighters (sorted by damage descending)
    public Page<Fighter> findStrongest(Pageable pageable) {
        return fighterRepository.findStrongestFighters(pageable);
    }

    // Get balanced fighters (health >= minHealth, damage <= maxDamage, sorted by resistance descending)
    public Page<Fighter> findBalanced(double minHealth, double maxDamage, Pageable pageable) {
        return fighterRepository.findBalancedFighters(minHealth, maxDamage, pageable);
    }

    // Get all fighters with pagination and sorting
    public Page<Fighter> findAll(Pageable pageable) {
        return fighterRepository.findAll(pageable);
    }

    // Save a new fighter (needed for CreateFighterController)
    public Fighter addFighter(Fighter fighter) {
        return fighterRepository.save(fighter);
    }
}