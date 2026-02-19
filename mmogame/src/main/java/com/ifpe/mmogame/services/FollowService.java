package com.ifpe.mmogame.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.ifpe.mmogame.dto.CharacterDTO;
import com.ifpe.mmogame.dto.NewFollowDTO;
import com.ifpe.mmogame.entities.User;
import com.ifpe.mmogame.entities.Character;
import com.ifpe.mmogame.entities.Follow;
import com.ifpe.mmogame.repositories.CharacterRepository;
import com.ifpe.mmogame.repositories.FollowRepository;
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

    public ResponseEntity<?> save(NewFollowDTO fDto) {
        User u = this.userRepo.findByEmail(this.jwtUtils.getAuthorizedId()).get();
        Character follower = this.characterRepo.findByIdAndUserId(fDto.getFollowerId(), u.getId()).get();
        Character following = this.characterRepo.findById(fDto.getFollowingId()).get();

        Follow f = new Follow();
        f.setFollower(follower);
        f.setFollowing(following);

        f = this.followRepo.save(f);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> showFollowers(int characterId) {
        List<Character> followers = this.followRepo.findFollowersByCharacterId(characterId);
        List<CharacterDTO> dtos = followers.stream().map(follower -> {
            CharacterDTO dto = new CharacterDTO();
            dto.setGameId(follower.getGame().getId());
            dto.setName(follower.getName());
            // dto.setPhotoId(follower.getPhotoId());
            dto.setUniqueName(follower.getUniqueName());
            return dto;
        }).toList();

        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<?> showFollowings(int characterId) {
        List<Character> followings = this.followRepo.findFollowingsByCharacterId(characterId);
        List<CharacterDTO> dtos = followings.stream().map(following -> {
            CharacterDTO dto = new CharacterDTO();
            dto.setGameId(following.getGame().getId());
            dto.setName(following.getName());
            // dto.setPhotoId(following.getPhotoId());
            dto.setUniqueName(following.getUniqueName());
            return dto;
        }).toList();

        return ResponseEntity.ok(dtos);
    }

}
