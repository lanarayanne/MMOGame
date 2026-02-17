package com.ifpe.mmogame.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifpe.mmogame.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{
    public Optional<User> findByEmail(String email);

}
