package com.example.Thymeleaf.Demo.repository;

import com.example.Thymeleaf.Demo.Model.Fighter;

//import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface FighterRepository extends JpaRepository<Fighter, Integer> {
    // return fighters containing the search term (case-insensitive)
    Page<Fighter> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // return fights with health geater than the specified value
    Page<Fighter> findByHealthGreaterThan(int health, Pageable pageable);

    // return strongest fighters based on damage (descending order)
    @Query("SELECT f FROM Fighter f ORDER BY f.damage DESC")
    Page<Fighter> findStrongestFighters(Pageable pageable);

    // return balanced fighters (high health, controlled damage)
    @Query("SELECT f FROM Fighter f WHERE f.health >= ?1 AND f.damage <= ?2 ORDER BY f.resistance DESC")
    Page<Fighter> findBalancedFighters(double minHealth, double maxDamage, Pageable pageable);
}
