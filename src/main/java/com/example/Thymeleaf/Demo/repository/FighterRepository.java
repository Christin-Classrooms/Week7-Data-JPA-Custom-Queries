package com.example.Thymeleaf.Demo.repository;

import com.example.Thymeleaf.Demo.Model.Fighter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FighterRepository extends JpaRepository<Fighter, Integer> {

    // 1. Find by name containing (case-insensitive)
    Page<Fighter> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // 2. Find by health greater than
    Page<Fighter> findByHealthGreaterThan(int health, Pageable pageable);

    // 3. Find strongest fighters ranked by damage (descending)
    @Query("SELECT f FROM Fighter f ORDER BY f.damage DESC")
    Page<Fighter> findStrongestFighters(Pageable pageable);

    // 4 Find balanced fighters
    @Query("SELECT f FROM Fighter f WHERE f.health >= ?1 AND f.damage <= ?2 ORDER BY f.resistance DESC")
    Page<Fighter> findBalancedFighters(double minHealth, double maxDamage, Pageable pageable);
}