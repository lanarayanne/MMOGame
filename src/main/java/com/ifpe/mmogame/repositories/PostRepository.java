package com.ifpe.mmogame.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ifpe.mmogame.entities.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByCharacterId(int characterId);
    @Query("SELECT p FROM Post p WHERE p.character.id <> :characterId")
    List<Post> findOtherCharactersPosts(@Param("characterId") int characterId);


}
