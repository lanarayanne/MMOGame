package com.ifpe.mmogame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ifpe.mmogame.dto.CharacterDTO;
import com.ifpe.mmogame.dto.NewFollowDTO;
import com.ifpe.mmogame.services.CharacterService;
import com.ifpe.mmogame.services.FollowService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/personagem")
public class CharacterController {
    @Autowired
    private CharacterService characterS;
    @Autowired
    private FollowService followS;

    @PostMapping("/novo")
    public ResponseEntity<?> createNewCharacter(@RequestBody CharacterDTO characterDTO) {
        return this.characterS.save(characterDTO);
    }

    @GetMapping("/lista")
    public ResponseEntity<?> getAllCharactersByUser() {
        return this.characterS.showAllCharactersByUser();
    }

    @GetMapping("/{characterId}")
    public ResponseEntity<?> getAllCharactersByGame(@PathVariable int characterId) {
        return this.characterS.showCharactersByGame(characterId);
    }

    @PostMapping("/seguir")
    public ResponseEntity<?> createNewFollow(@RequestBody NewFollowDTO followDTO) {
        return this.followS.save(followDTO);
    }

    @GetMapping("/{characterId}/seguindo")
    public ResponseEntity<?> getFollowing(@PathVariable int characterId) {
        return this.followS.showFollowings(characterId);
    }

    @GetMapping("/{characterId}/seguidores")
    public ResponseEntity<?> getFollowers(@PathVariable int characterId) {
        return this.followS.showFollowers(characterId);
    }

    @PatchMapping("/foto/{characterId}")
    public ResponseEntity<?> uploadPhoto(@RequestParam("file") MultipartFile file, @PathVariable int characterId) {
        return this.characterS.uploadPhoto(file, characterId);
    }

    @GetMapping("/perfil/{characterId}")
    public ResponseEntity<?> getPerfil(@PathVariable int characterId) {
        return this.characterS.getPerfil(characterId);
    }

    @GetMapping("/jogo/{gameId}")
    public ResponseEntity<?> getCharactersByGameId(@PathVariable int gameId) {
        return this.characterS.showCharactersByGameId(gameId);
    }

}
