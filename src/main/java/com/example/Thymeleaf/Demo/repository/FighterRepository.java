package com.example.Thymeleaf.Demo.repository;

import com.example.Thymeleaf.Demo.Model.Fighter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FighterRepository extends JpaRepository<Fighter, Integer> {
    // Derived query method
    Page<Fighter> findByNameContainingIgnoreCase(String name, Pageable page);

    // Derived query method
    Page<Fighter> findByHealthGreaterThan(int health, Pageable page);

    // @Query Methods
    @Query("SELECT f FROM Fighter f ORDER BY f.damage DESC")
    Page<Fighter> findStrongestFighters(Pageable pageable);

    // @Query Methods with parameters
    @Query("SELECT f FROM Fighter f WHERE f.health >= ?1 AND f.damage <= ?2")
    Page<Fighter> findBalancedFighters(double minHealth, double maxDamage, Pageable pageable);
}