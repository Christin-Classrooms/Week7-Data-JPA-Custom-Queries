package com.example.Thymeleaf.Demo.repository;

import com.example.Thymeleaf.Demo.Model.Fighter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FighterRepository extends JpaRepository<Fighter, Integer> {

    Page<Fighter> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Fighter> findByHealthGreaterThan(int health, Pageable pageable);

    
    @Query("Select f from Fighter f order by f.damage DESC")
    Page<Fighter> findStrongestFighters(Pageable pageable);

    @Query("Select f from Fighter f where f.health >= :minHealth AND f.damage <= :maxDamage order by f.resistance DESC")
    Page<Fighter> findBalancedFighters(@Param("minHealth") double minHealth,@Param("maxDamage") double maxDamage,Pageable pageable);

}
