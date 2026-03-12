package com.example.Thymeleaf.Demo.Service;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.repository.FighterRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FighterService {

    private final FighterRepository fighterRepository;

    public FighterService(FighterRepository fighterRepository) {
        this.fighterRepository = fighterRepository;
    }

    public List<Fighter> getAllFighters() {
        return fighterRepository.findAll();
    }

    public void addFighter(Fighter fighter) {
        fighterRepository.save(fighter);
    }

    public Optional<Fighter> getFighterById(int id) {
        return fighterRepository.findById(id);
    }

    public void deleteFighter(int id) {
        fighterRepository.deleteById(id);
    }

    public boolean existsFighter(int id) {
        return fighterRepository.existsById(id);
    }

    public long countFighters() {
        return fighterRepository.count();
    }

    // Pagination - get all fighters
    public Page<Fighter> findAll(Pageable pageable) {
        return fighterRepository.findAll(pageable);
    }

    // Search by name
    public Page<Fighter> findByNameContainingIgnoreCase(String name, Pageable pageable) {
        return fighterRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    // Filter by health
    public Page<Fighter> findByHealthGreaterThan(int health, Pageable pageable) {
        return fighterRepository.findByHealthGreaterThan(health, pageable);
    }

    // Strongest fighters
    public Page<Fighter> findStrongestFighters(Pageable pageable) {
        return fighterRepository.findStrongestFighters(pageable);
    }

    // Balanced fighters
    public Page<Fighter> findBalancedFighters(double minHealth, double maxDamage, Pageable pageable) {
        return fighterRepository.findBalancedFighters(minHealth, maxDamage, pageable);
    }

}