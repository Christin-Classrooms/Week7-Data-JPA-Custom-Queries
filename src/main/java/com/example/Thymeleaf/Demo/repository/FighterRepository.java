package com.example.Thymeleaf.Demo.repository;

import com.example.Thymeleaf.Demo.Model.Fighter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FighterRepository extends JpaRepository<Fighter, Integer> {

    // Search fighters by name (case-insensitive)
    Page<Fighter> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Find fighters with health greater than a value
    Page<Fighter> findByHealthGreaterThan(int health, Pageable pageable);

    // Custom JPQL query for strongest fighters (sorted by damage descending)
    @Query("SELECT f FROM Fighter f ORDER BY f.damage DESC")
    Page<Fighter> findStrongestFighters(Pageable pageable);

    // Custom JPQL query for balanced fighters
    @Query("SELECT f FROM Fighter f WHERE f.health >= ?1 AND f.damage <= ?2 ORDER BY f.resistance DESC")
    Page<Fighter> findBalancedFighters(double minHealth, double maxDamage, Pageable pageable);

}