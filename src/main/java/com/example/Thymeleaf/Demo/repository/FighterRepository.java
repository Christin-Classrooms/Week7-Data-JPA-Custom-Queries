package com.example.Thymeleaf.Demo.repository;

import com.example.Thymeleaf.Demo.Model.Fighter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface FighterRepository extends JpaRepository<Fighter, Integer> {

    Page<Fighter> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Fighter> findByHealthGreaterThan(int health, Pageable pageable);

    @Query("SELECT f FROM Fighter f ORDER BY f.damage DESC")
    Page<Fighter> findStrongestFighters(Pageable pageable);

    @Query("SELECT f FROM Fighter f WHERE f.health BETWEEN 40 AND 70 AND f.damage BETWEEN 40 AND 70")
    Page<Fighter> findBalancedFighters(Pageable pageable);

    @Query("SELECT f FROM Fighter f WHERE f.resistance > :value")
    Page<Fighter> findWithHighResistance(@Param("value") int value, Pageable pageable);

    @Query(value = "SELECT * FROM fighter WHERE health < :maxHealth", nativeQuery = true)
    Page<Fighter> findWeakFighters(@Param("maxHealth") int maxHealth, Pageable pageable);

    @Query("SELECT f FROM Fighter f ORDER BY (f.health + f.damage + f.resistance) DESC")
    Page<Fighter> findTopOverallStats(Pageable pageable);
}
