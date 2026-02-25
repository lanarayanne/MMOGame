package com.ifpe.mmogame.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.ifpe.mmogame.dto.CharacterDTO;
import com.ifpe.mmogame.dto.NewFollowDTO;
import com.ifpe.mmogame.entities.User;
import com.ifpe.mmogame.entities.Character;
import com.ifpe.mmogame.entities.Follow;
import com.ifpe.mmogame.entities.Photo;
import com.ifpe.mmogame.repositories.CharacterRepository;
import com.ifpe.mmogame.repositories.FollowRepository;
import com.ifpe.mmogame.repositories.PhotoRepository;
import com.ifpe.mmogame.repositories.UserRepository;
import com.ifpe.mmogame.security.JwtUtils;

@Component
public class FollowService {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private CharacterRepository characterRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private FollowRepository followRepo;
    @Autowired
    private PhotoRepository photoRepo;

    public ResponseEntity<?> save(NewFollowDTO fDto) {
        try {
            User u = this.userRepo.findByEmail(this.jwtUtils.getAuthorizedId()).get();
            Character follower = this.characterRepo.findByIdAndUserId(fDto.getFollowerId(), u.getId()).get();
            Character following = this.characterRepo.findById(fDto.getFollowingId()).get();

            Follow f = new Follow();
            f.setFollower(follower);
            f.setFollowing(following);

            f = this.followRepo.save(f);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> showFollowers(int characterId) {
        try {
            List<Character> followers = this.followRepo.findFollowersByCharacterId(characterId);
            List<CharacterDTO> dtos = followers.stream().map(follower -> {
                CharacterDTO dto = new CharacterDTO();
                dto.setId(follower.getId());
                dto.setGameId(follower.getGame().getId());
                dto.setName(follower.getName());

                Optional<Photo> optPhoto = this.photoRepo.findByCharacterId(follower.getId());

                if (optPhoto.isPresent()) {
                    dto.setPhotoContent(optPhoto.get().getContent());
                    dto.setPhotoExtension(optPhoto.get().getExtension());
                }
                
                dto.setUniqueName(follower.getUniqueName());
                return dto;
            }).toList();

            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> showFollowings(int characterId) {
        try {
            List<Character> followings = this.followRepo.findFollowingsByCharacterId(characterId);
            List<CharacterDTO> dtos = followings.stream().map(following -> {
                CharacterDTO dto = new CharacterDTO();
                dto.setId(following.getId());
                dto.setGameId(following.getGame().getId());
                dto.setName(following.getName());

                Optional<Photo> optPhoto = this.photoRepo.findByCharacterId(following.getId());

                if (optPhoto.isPresent()) {
                    dto.setPhotoContent(optPhoto.get().getContent());
                    dto.setPhotoExtension(optPhoto.get().getExtension());
                }

                dto.setUniqueName(following.getUniqueName());
                return dto;
            }).toList();

            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
