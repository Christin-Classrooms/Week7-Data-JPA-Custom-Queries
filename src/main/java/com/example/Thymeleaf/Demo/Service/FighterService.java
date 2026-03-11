package com.example.Thymeleaf.Demo.Service;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.repository.FighterRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FighterService {

    private final FighterRepository fighterRepository;

    public FighterService(FighterRepository fighterRepository) {
        this.fighterRepository = fighterRepository;
    }

    // ------------------ PAGINATION / FILTER METHODS ------------------

    public Page<Fighter> getAllFighters(Pageable pageable) {
        return fighterRepository.findAll(pageable);
    }

    public Page<Fighter> searchByName(String name, Pageable pageable) {
        return fighterRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Page<Fighter> filterByHealth(int health, Pageable pageable) {
        return fighterRepository.findByHealthGreaterThan(health, pageable);
    }

    public Page<Fighter> getStrongestFighters(Pageable pageable) {
        return fighterRepository.findStrongestFighters(pageable);
    }

    public Page<Fighter> getBalancedFighters(double minHealth, double maxDamage, Pageable pageable) {
        return fighterRepository.findBalancedFighters(minHealth, maxDamage, pageable);
    }

    // ------------------ ORIGINAL CRUD METHODS ------------------

    public void addFighter(Fighter fighter) {
        fighterRepository.save(fighter);
    }

    public Optional<Fighter> getFighterById(int id) {
        return fighterRepository.findById((long) id);
    }

    public void deleteFighter(int id) {
        fighterRepository.deleteById((long) id);
    }

    public boolean existsFighter(int id) {
        return fighterRepository.existsById((long) id);
    }

    public long countFighters() {
        return fighterRepository.count();
    }
}