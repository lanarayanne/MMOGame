package com.ifpe.mmogame.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.ifpe.mmogame.entities.Photo;
import com.ifpe.mmogame.entities.User;
import com.ifpe.mmogame.dto.CharacterDTO;
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


    public ResponseEntity<?> save(CharacterDTO cDto) {

        try {
            if (cDto.getName().isEmpty() || cDto.getUniqueName().isEmpty() || cDto.getGameId() == null) {
                return ResponseEntity.badRequest().build();
            }

            Game g = this.gameRepo.findById(cDto.getGameId()).get(); 

            Character c = new Character();
            c.setGame(g);
            c.setName(cDto.getName());
            c.setUniqueName(cDto.getUniqueName());
            User u = this.userRepo.findByEmail(this.jwtUtils.getAuthorizedId()).get();
            c.setUser(u);

            c = this.characterRepo.save(c);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    public ResponseEntity<?> showAllCharactersByUser() {

        try {

            User u = this.userRepo.findByEmail(this.jwtUtils.getAuthorizedId()).get();

            List<Character> characters = this.characterRepo.findByUserId(u.getId());

            List<CharacterDTO> dtoList = characters.stream()
                    .map(c -> {
                        CharacterDTO dto = new CharacterDTO();
                        dto.setGameId(c.getGame().getId());
                        dto.setName(c.getName());
                        dto.setUniqueName(c.getUniqueName());
                        return dto;
                    })
                    .toList();

            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    public ResponseEntity<?> showCharactersByGame(int characterId) {

        try {

            User u = this.userRepo.findByEmail(this.jwtUtils.getAuthorizedId()).get();
            Character character = new Character();
            character = this.characterRepo.findById(characterId).get();

            List<Character> characters = this.characterRepo.findByGame(character.getGame());

            List<CharacterDTO> dtoList = characters.stream()
                    .filter(item -> item.getId() != characterId)
                    .map(c -> {
                        CharacterDTO dto = new CharacterDTO();
                        dto.setGameId(c.getGame().getId());
                        dto.setName(c.getName());
                        dto.setUniqueName(c.getUniqueName());
                        return dto;
                    })
                    .toList();

            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    public ResponseEntity<?> uploadPhoto(MultipartFile file, int characterId) {

        Photo p = new Photo();

        try {
            p.setContent(file.getBytes());

            p.setExtension(file.getContentType().split("/")[1]);

            p.setLength((int) file.getSize());

            String userId = this.jwtUtils.getAuthorizedId();

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

        public ResponseEntity<?> getPerfil(int characterId) {
        String userEmail = this.jwtUtils.getAuthorizedId();
        User user = this.userRepo.findByEmail(userEmail).get();
        Character c = this.characterRepo.findByIdAndUserId(characterId, user.getId()).get();

        Optional<Photo> photoOpt = this.photoRepo.findByCharacterId(c.getId());

        if (photoOpt.isPresent()) {
            Photo p = photoOpt.get();
            p.setCharacter(c);
            return ResponseEntity.ok(photoOpt.get());
        }

        return ResponseEntity.notFound().build();
    }

}
