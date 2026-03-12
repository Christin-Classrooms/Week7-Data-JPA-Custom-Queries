package com.example.Thymeleaf.Demo.Service;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.repository.FighterRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public Page<Fighter> getFightersPaginated(int page, int size, String search, String filterType) {
        Pageable pageable = PageRequest.of(page, size);

        if (search != null && !search.isEmpty()) {
            return fighterRepository.findByNameContainingIgnoreCase(search, pageable);
        }

        if ("healthy".equalsIgnoreCase(filterType)) {
            return fighterRepository.findByHealthGreaterThan(1200, pageable);
        }

        if ("strongest".equalsIgnoreCase(filterType)) {
            return fighterRepository.findStrongestFighters(pageable);
        }

        if ("balanced".equalsIgnoreCase(filterType)) {
            return fighterRepository.findBalancedFighters(1100, 50.0, pageable);
        }

        return fighterRepository.findAll(pageable);
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