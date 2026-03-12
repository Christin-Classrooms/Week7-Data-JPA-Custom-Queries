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

    public Page<Fighter> findByName(String search, Pageable pageable) {
        return fighterRepository.findByNameContainingIgnoreCase(search, pageable);
        
    }

    public Page<Fighter> findByHealth(int i, Pageable pageable) {
        return fighterRepository.findByHealthGreaterThan(i, pageable);
    }

    public Page<Fighter> findStrongest(Pageable pageable) {
        return fighterRepository.findStrongestFighters(pageable);
    }

    public Page<Fighter> findBalanced(int i, int j, Pageable pageable) {
        return fighterRepository.findBalancedFighters(i, j, pageable);
    }

    public Page<Fighter> getAllFighters(Pageable pageable) {
        return fighterRepository.findAll(pageable);
    }
    }

    

