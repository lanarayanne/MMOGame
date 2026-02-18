package com.ifpe.mmogame.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ifpe.mmogame.entities.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {
    @Query("SELECT p FROM Photo p WHERE p.user.email =: email")
    public Optional<Photo> findByEmailUser(@Param("email") String email);

}
