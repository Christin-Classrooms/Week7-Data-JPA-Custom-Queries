package com.example.Thymeleaf.Demo.repository;

import com.example.Thymeleaf.Demo.Model.Fighter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FighterRepository extends JpaRepository<Fighter, Integer> {

    // A) Name contains (case-insensitive)
    Page<Fighter> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // B) Health greater than
    Page<Fighter> findByHealthGreaterThan(int health, Pageable pageable);

    // C) Strongest fighters by damage (DESC)
    @Query("SELECT f FROM Fighter f ORDER BY f.damage DESC")
    Page<Fighter> findStrongestFighters(Pageable pageable);

    // D) Balanced fighters: health >= minHealth AND damage <= maxDamage, sorted by resistance DESC
    @Query("""
           SELECT f FROM Fighter f
           WHERE f.health >= :minHealth AND f.damage <= :maxDamage
           ORDER BY f.resistance DESC
           """)
    Page<Fighter> findBalancedFighters(@Param("minHealth") double minHealth,
                                       @Param("maxDamage") double maxDamage,
                                       Pageable pageable);
}