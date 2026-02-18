package com.ifpe.mmogame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifpe.mmogame.dto.LoginDTO;
import com.ifpe.mmogame.dto.UserDTO;
import com.ifpe.mmogame.entities.User;
import com.ifpe.mmogame.services.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authS;

    @PostMapping
    public ResponseEntity<?> postNewUser(@RequestBody UserDTO user) {
        return this.authS.newUser(user); 
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginMethod(@RequestBody LoginDTO login) {
        return this.authS.login(login);
    }


}
