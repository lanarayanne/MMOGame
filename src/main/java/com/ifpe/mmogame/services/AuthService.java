package com.ifpe.mmogame.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ifpe.mmogame.dto.LoginDTO;
import com.ifpe.mmogame.entities.User;
import com.ifpe.mmogame.repositories.UserRepository;
import com.ifpe.mmogame.security.JwtUtils;

@Component
public class AuthService {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder encoder;

    public ResponseEntity<?> newUser(User user){

        user.setPassword(this.encoder.encode(user.getPassword()));

        this.userRepo.save(user);
        
        return ResponseEntity.ok("Success!");

    }

    public ResponseEntity<?> login(LoginDTO login){
        
        Optional<User> userOpt = this.userRepo.findByEmail(login.getEmail());

        if(userOpt.isPresent()){

            User user = userOpt.get();

            if(this.encoder.matches(login.getPassword(), user.getPassword())){
                return ResponseEntity.ok(this.jwtUtils
                    .generateToken(user.getEmail(), "USER"));
            }

        }

        return ResponseEntity.badRequest().body("Invalid Credentials");

    }


}
