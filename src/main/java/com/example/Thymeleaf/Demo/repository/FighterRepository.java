package com.example.Thymeleaf.Demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Thymeleaf.Demo.Model.Fighter;

@Repository
public interface FighterRepository extends JpaRepository<Fighter, Integer> {

    Page<Fighter> findByNameContainingIgnoringCase(String name, Pageable pageable);

    Page<Fighter> findByHealthGreaterThan(int health, Pageable pageable);

    Page<Fighter> findByIdContaining(int id, Pageable pageable);
    
    @Query("SELECT f FROM Fighter f ORDER BY f.damage DESC")
    Page<Fighter> findStrongestFighters(Pageable pageable);


    @Query("SELECT f FROM Fighter f WHERE f.health >= :minHealth AND f.damage <= :maxDamage ORDER BY f.resistance DESC")
    Page<Fighter> findBalancedFighters(@Param("minHealth") double minHealth, @Param("maxDamage") double maxDamage,  Pageable pageable);
}
