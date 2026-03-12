package com.example.Thymeleaf.Demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.Thymeleaf.Demo.Model.Fighter;

@Repository
public interface FighterRepository extends JpaRepository<Fighter, Integer> {

    // Search fighters by name (case insensitive)
    Page<Fighter> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Fighters with health greater than given value
    Page<Fighter> findByHealthGreaterThan(int health, Pageable pageable);

    // Strongest fighters ordered by damage
    @Query("SELECT f FROM Fighter f ORDER BY f.damage DESC")
    Page<Fighter> findStrongestFighters(Pageable pageable);

    // Balanced fighters
    @Query("SELECT f FROM Fighter f WHERE f.health >= ?1 AND f.damage <= ?2 ORDER BY f.resistance DESC")
    Page<Fighter> findBalancedFighters(double minHealth, double maxDamage, Pageable pageable);

}
