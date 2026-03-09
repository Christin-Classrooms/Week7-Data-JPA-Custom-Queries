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

    public Page<Fighter> findByName(String name, Pageable page) { return fighterRepository.findByNameContainingIgnoreCase(name,page); }

    public Page<Fighter> findByHealthGreaterThan(int minHealth, Pageable page) { return fighterRepository.findByHealthGreaterThan(minHealth, page); }

    public Page<Fighter> findStrongestFighters(Pageable page) { return fighterRepository.findStrongestFighters(page); }

    public Page<Fighter> findBalancedFighters(double minHealthValue, double maxDamage, Pageable page) { return fighterRepository.findBalancedFighters(minHealthValue, maxDamage, page); }

    public Page<Fighter> getAllFightersPageable(Pageable page) { return fighterRepository.findAll(page); }
}
