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

    private FighterRepository fighterRepository;

    public FighterService(FighterRepository fighterRepository) {
        this.fighterRepository = fighterRepository;
    }

    public List<Fighter> getAllFighters() {
        return fighterRepository.findAll();
    }

    public Page<Fighter> getAllFightersPageable(Pageable pageable) {
        return fighterRepository.findAll(pageable);
    }

    public void addFighter(Fighter fighter) {
        fighterRepository.save(fighter);
    }

    public Page<Fighter> findFighterByName(String name,Pageable page) {
        return fighterRepository.findByNameContainingIgnoreCase(name,page);
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

    public Page<Fighter> findFightersByHealthGreaterThan(int threshold, Pageable pageable){
        return fighterRepository.findByHealthGreaterThan(threshold, pageable);
    }
	public Page<Fighter> findStrongestFighters(Pageable pageable){
        return fighterRepository.findStrongestFighters(pageable);
    }
    public Page<Fighter> findBalancedFighters(double minHealth, double maxDamage, Pageable pageable){
        return fighterRepository.findBalancedFighters(minHealth, maxDamage, pageable);
    }

}
