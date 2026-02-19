package com.ifpe.mmogame.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ifpe.mmogame.entities.Like;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    public List<Like> findByPostId(int postId);

    public List<Like> findByCharacterId(int characterId);

}
