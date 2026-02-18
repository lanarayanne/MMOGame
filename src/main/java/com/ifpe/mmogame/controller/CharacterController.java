package com.ifpe.mmogame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifpe.mmogame.dto.CharacterDTO;
import com.ifpe.mmogame.services.CharacterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/character")
public class CharacterController {
    @Autowired
    private CharacterService characterS;


    @PostMapping("/novo")
    public ResponseEntity<?> createNewCharacter(@RequestBody CharacterDTO characterDTO) {
        return this.characterS.save(characterDTO);
    }

    @GetMapping("list")
    public ResponseEntity<?> getAllCharactersByUser() {
        return this.characterS.showAllCharactersByUser();
    }
    

}
