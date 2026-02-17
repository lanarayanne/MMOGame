package com.ifpe.mmogame.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifpe.mmogame.entities.Character;

import java.util.List;


public interface CharacterRepository extends JpaRepository<Character, Integer> {
        Optional<Character> findByUniqueName(String uniqueName);
        List<Character> findByGame (String game);
        List<Character> findByUserId(Integer userId);

}
