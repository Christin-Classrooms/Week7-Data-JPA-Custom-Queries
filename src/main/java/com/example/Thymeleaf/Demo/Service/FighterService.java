package com.example.Thymeleaf.Demo.Service;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.repository.FighterRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    
    // the paginated version of getAllFighters
    public Page<Fighter> getAllFightersPaginated(int page, int size, String sort, String direction) {
        Sort sortOrder = direction.equalsIgnoreCase("ASC") ? 
            Sort.by(sort).ascending() : Sort.by(sort).descending();
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        return fighterRepository.findAll(pageable);
    }

    // searching by name
    public Page<Fighter> searchFightersByName(String name, int page, int size, String sort, String direction) {
        Sort sortOrder = direction.equalsIgnoreCase("ASC") ? 
            Sort.by(sort).ascending() : Sort.by(sort).descending();
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        return fighterRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    // filtering by health
    public Page<Fighter> getFightersByHealthGreaterThan(int health, int page, int size, String sort, String direction) {
        Sort sortOrder = direction.equalsIgnoreCase("ASC") ? 
            Sort.by(sort).ascending() : Sort.by(sort).descending();
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        return fighterRepository.findByHealthGreaterThan(health, pageable);
    }

    // getting strongest fighters, note: sorting is fixed by damage DESC
    public Page<Fighter> getStrongestFighters(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return fighterRepository.findStrongestFighters(pageable);
    }

    // getting balanced fighters via pagination
    public Page<Fighter> getBalancedFighters(double minHealth, double maxDamage, int page, int size, String sort, String direction) {
        Sort sortOrder = direction.equalsIgnoreCase("ASC") ? 
            Sort.by(sort).ascending() : Sort.by(sort).descending();
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        return fighterRepository.findBalancedFighters(minHealth, maxDamage, pageable);
    }

    // note to self: dont erase below
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