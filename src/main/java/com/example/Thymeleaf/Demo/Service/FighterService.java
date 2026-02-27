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

    public List<Fighter> getAllFighters() {
        return fighterRepository.findAll();
    }

    public Page<Fighter> getAllFightersPageable(Pageable pageable){
        return fighterRepository.findAll(pageable);
    }

    public void addFighter(Fighter fighter) {
        fighterRepository.save(fighter);
    }

    public Page<Fighter> findFighterByName(String name, Pageable page){
        return fighterRepository.findByNameContainingIgnoringCase(name,page);
    }

    public Page<Fighter> getFightersByIdContaining(int id, Pageable pageable){
        return fighterRepository.findByIdContaining(id, pageable);
    }

    public Page<Fighter> getFightersByHealthGreaterThan(int health, Pageable pageable){
        return fighterRepository.findByHealthGreaterThan(health, pageable);
    }

    public Page<Fighter> getStrongestFighters(Pageable pageable){
        return fighterRepository.findStrongestFighters(pageable);
    }

    public Page<Fighter> getBalancedFighters(double minHealth, double maxDamage, Pageable pageable){
        return fighterRepository.findBalancedFighters(minHealth, maxDamage, pageable);
    }

    public Fighter getFighterById(int id) {
        return fighterRepository.findById(id).orElse(null);
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
