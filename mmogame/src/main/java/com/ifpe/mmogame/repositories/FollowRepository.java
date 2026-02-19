package com.ifpe.mmogame.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ifpe.mmogame.entities.Follow;
import com.ifpe.mmogame.entities.Character;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
    @Query("SELECT f.follower FROM Follow f WHERE f.following.id = :id")
    List<Character> findFollowersByCharacterId(@Param("id") Integer id);

    @Query("SELECT f.following FROM Follow f WHERE f.follower.id = :id")
    List<Character> findFollowingsByCharacterId(@Param("id") Integer id);

}
