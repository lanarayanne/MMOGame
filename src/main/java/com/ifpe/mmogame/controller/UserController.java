package com.ifpe.mmogame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ifpe.mmogame.dto.CharacterDTO;
import com.ifpe.mmogame.dto.NewPasswordDTO;
import com.ifpe.mmogame.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userS;

    @GetMapping
    public ResponseEntity<?> getUser() {
        return this.userS.getUser();
    }

    @PatchMapping("/password")
    public ResponseEntity<?> newPassword (@RequestBody NewPasswordDTO newPass){
        return this.userS.updatePassword(newPass);
    }

    
    

}
