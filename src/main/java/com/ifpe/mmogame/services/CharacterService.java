package com.ifpe.mmogame.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.ifpe.mmogame.entities.Photo;
import com.ifpe.mmogame.entities.User;
import com.ifpe.mmogame.dto.NewCharacterDTO;
import com.ifpe.mmogame.entities.Character;
import com.ifpe.mmogame.entities.Game;
import com.ifpe.mmogame.repositories.CharacterRepository;
import com.ifpe.mmogame.repositories.GameRepository;
import com.ifpe.mmogame.repositories.PhotoRepository;
import com.ifpe.mmogame.repositories.UserRepository;
import com.ifpe.mmogame.security.JwtUtils;

@Component
public class CharacterService {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CharacterRepository characterRepo;
    @Autowired
    private PhotoRepository photoRepo;
    @Autowired
    private GameRepository gameRepo;
    @Autowired
    private PasswordEncoder encoder;

    public ResponseEntity<?> save (NewCharacterDTO cDto){
        if(cDto.getName().isEmpty() || cDto.getUniqueName().isEmpty() || cDto.getGameId() == null) {
            return ResponseEntity.badRequest().build();
        }

        Game g = new Game();
        g=this.gameRepo.findById(cDto.getGameId()).get();

        Character c = new Character();
        c.setGame(g);
        c.setName(cDto.getName());
        c.setUniqueName(cDto.getUniqueName());
        User u = this.userRepo.findByEmail(this.jwtUtils.getAuthorizedId()).get();
        c.setUser(u);

        c = this.characterRepo.save(c);

        return ResponseEntity.ok().build();

    }





    public ResponseEntity<?> uploadPhoto(MultipartFile file, int characterId) {

        Photo p = new Photo();

        try {
            p.setContent(file.getBytes());
            
            p.setExtension(file.getContentType().split("/")[1]);

            p.setLength((int) file.getSize());

            // String userId = this.jwtUtils.getAuthorizedId();

            // User user = this.userRepo.findByEmail(userId).get();

            Character c = this.characterRepo.findById(characterId).get();

            p.setCharacter(c);

            photoRepo.save(p);

            return ResponseEntity.ok().build();
        } catch (IOException e) {
            
            e.printStackTrace();
        }

        return ResponseEntity.internalServerError().build();

    }


}
