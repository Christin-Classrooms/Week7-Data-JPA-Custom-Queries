package com.example.Thymeleaf.Demo.Service;

import java.util.List;
import java.util.Optional;

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

    public void addFighter(Fighter fighter) {
        fighterRepository.save(fighter);
    }

    public Optional<Fighter> getFighterById(Long id) {
        return fighterRepository.findById(id);
    }

    public void deleteFighter(Long id) {
        fighterRepository.deleteById(id);
    }

    public boolean existsFighter(Long id) {
        return fighterRepository.existsById(id);
    }
}