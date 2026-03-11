package com.example.Thymeleaf.Demo.Service;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.repository.FighterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FighterService {

    @Autowired
    private FighterRepository fighterRepository;

    public List<Fighter> getAllFighters() {
        return fighterRepository.findAll();
    }

    public Page<Fighter> getAllFightersPageable(Pageable pageable) {
        return fighterRepository.findAll(pageable);
    }

    public Page<Fighter> findFighterByName(String name, Pageable pageable) {
        return fighterRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Page<Fighter> findFightersByHealthGreaterThan(int health, Pageable pageable) {
        return fighterRepository.findByHealthGreaterThan(health, pageable);
    }

    public Page<Fighter> findStrongestFighters(Pageable pageable) {
        return fighterRepository.findStrongestFighters(pageable);
    }

    public Page<Fighter> findBalancedFighters(double minHealth, double maxDamage, Pageable pageable) {
        return fighterRepository.findBalancedFighters(minHealth, maxDamage, pageable);
    }

    public void addFighter(Fighter fighter) {
        fighterRepository.save(fighter);
    }
}