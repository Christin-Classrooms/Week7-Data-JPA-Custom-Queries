package com.example.Thymeleaf.Demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.Thymeleaf.Demo.Model.Fighter;

@Repository
public interface FighterRepository extends JpaRepository<Fighter, Integer> {

    // Derived query: fighters whose name contains the search term (case-insensitive)
    Page<Fighter> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Derived query: fighters with health greater than the specified value
    Page<Fighter> findByHealthGreaterThan(int health, Pageable pageable);

    // JPQL: strongest fighters ranked by damage descending
    @Query("SELECT f FROM Fighter f ORDER BY f.damage DESC")
    Page<Fighter> findStrongestFighters(Pageable pageable);

    // JPQL: balanced fighters with high health and controlled damage
    @Query("SELECT f FROM Fighter f WHERE f.health >= ?1 AND f.damage <= ?2 ORDER BY f.resistance DESC")
    Page<Fighter> findBalancedFighters(double minHealth, double maxDamage, Pageable pageable);
}
