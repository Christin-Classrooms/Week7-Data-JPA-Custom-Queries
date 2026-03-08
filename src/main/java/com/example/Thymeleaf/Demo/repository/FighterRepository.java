package com.example.Thymeleaf.Demo.repository;

import com.example.Thymeleaf.Demo.Model.Fighter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface FighterRepository extends JpaRepository<Fighter, Integer> {
Page<Fighter> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
