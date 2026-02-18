package com.ifpe.mmogame.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifpe.mmogame.entities.Game;

public interface GameRepository extends JpaRepository<Game, Integer> {
    
}
