// Name: Fahad Arif (N01729165)
// Course: Web Application Development (CPAN-228)

package com.example.Thymeleaf.Demo.repository;

import com.example.Thymeleaf.Demo.Model.Fighter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FighterRepository extends JpaRepository<Fighter, Long> {
    Page<Fighter> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Fighter> findByHealthGreaterThan(int health, Pageable pageable);

    @Query("SELECT f FROM Fighter f ORDER BY f.damage DESC")
    Page<Fighter> findStrongestFighters(Pageable pageable);

    @Query("SELECT f FROM Fighter f " +
           "WHERE f.health >= :minHealth AND f.damage <= :maxDamage " +
           "ORDER BY f.resistance DESC")
    Page<Fighter> findBalancedFighters(@Param("minHealth") double minHealth, @Param("maxDamage") double maxDamage, Pageable pageable);
}