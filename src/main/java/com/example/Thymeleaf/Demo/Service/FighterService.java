package com.example.Thymeleaf.Demo.Service;

import java.util.List;
import java.util.Optional;

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

    public List<Fighter> getAllFighters() {
        return fighterRepository.findAll();
    }

    public Page<Fighter> getFightersPaged(Pageable pageable) {
        return fighterRepository.findAll(pageable);
    }

    public Page<Fighter> searchByName(String name, Pageable pageable) {
        return fighterRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Page<Fighter> getByHealthGreaterThan(int health, Pageable pageable) {
        return fighterRepository.findByHealthGreaterThan(health, pageable);
    }

    public Page<Fighter> getStrongestFighters(Pageable pageable) {
        return fighterRepository.findStrongestFighters(pageable);
    }

    public Page<Fighter> getBalancedFighters(double minHealth, double maxDamage, Pageable pageable) {
        return fighterRepository.findBalancedFighters(minHealth, maxDamage, pageable);
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

}
