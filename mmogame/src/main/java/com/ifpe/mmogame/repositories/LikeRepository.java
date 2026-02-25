package com.ifpe.mmogame.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ifpe.mmogame.entities.Like;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    public List<Like> findByPostId(int postId);

    public List<Like> findByCharacterId(int characterId);

    Optional<Like> findByCharacterIdAndPostId(int characterId, int postId);
    void deleteByCharacterIdAndPostId(int characterId, int postId);

    boolean existsByPostIdAndCharacterId(int postId, int characterId);

}
