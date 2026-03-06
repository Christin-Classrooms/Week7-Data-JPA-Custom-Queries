package com.example.Thymeleaf.Demo.repository;

import com.example.Thymeleaf.Demo.Model.Fighter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FighterRepository extends JpaRepository<Fighter, Integer> {

    // 1. Returns fighters whose name contains the search term
    Page<Fighter> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // 2. Returns fighters with health greater than the specified value
    Page<Fighter> findByHealthGreaterThan(int health, Pageable pageable);

    // 3. Returns the strongest fighters ranked by damage
    @Query("SELECT f FROM Fighter f ORDER BY f.damage DESC")
    Page<Fighter> findStrongestFighters(Pageable pageable);

    // 4. Finds fighters with high health but controlled damage
    @Query("SELECT f FROM Fighter f WHERE f.health >= ?1 AND f.damage <= ?2 ORDER BY f.resistance DESC")
    Page<Fighter> findBalancedFighters(double minHealth, double maxDamage, Pageable pageable);

}