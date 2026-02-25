package com.ifpe.mmogame.services;

import java.nio.file.OpenOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.ifpe.mmogame.dto.LikeDTO;
import com.ifpe.mmogame.dto.NewLikeDTO;
import com.ifpe.mmogame.entities.User;
import com.ifpe.mmogame.repositories.CharacterRepository;
import com.ifpe.mmogame.repositories.LikeRepository;
import com.ifpe.mmogame.repositories.PostRepository;
import com.ifpe.mmogame.repositories.UserRepository;
import com.ifpe.mmogame.security.JwtUtils;
import com.ifpe.mmogame.entities.Character;
import com.ifpe.mmogame.entities.Like;
import com.ifpe.mmogame.entities.Post;

@Component
public class LikeService {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private CharacterRepository characterRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private LikeRepository likeRepo;

    public ResponseEntity<?> save(NewLikeDTO lDto) {
        try {
            User u = this.userRepo.findByEmail(this.jwtUtils.getAuthorizedId()).get();
            Character c = this.characterRepo.findByIdAndUserId(lDto.getCharacterId(), u.getId()).get();
            Post p = this.postRepo.findById(lDto.getPostId()).get();

            if (likeRepo.existsByPostIdAndCharacterId(p.getId(), c.getId())) {
                return ResponseEntity.badRequest().build();
            }

            Like l = new Like();
            l.setCharacter(c);
            l.setPost(p);
            l = this.likeRepo.save(l);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> showLikesByCharacter(int characterId) {
        try {
            User u = this.userRepo.findByEmail(this.jwtUtils.getAuthorizedId()).get();
            Character c = this.characterRepo.findByIdAndUserId(characterId, u.getId()).get();

            List<Like> likes = this.likeRepo.findByCharacterId(c.getId());
            List<LikeDTO> dtos = likes.stream().map(like -> {
                LikeDTO dto = new LikeDTO();
                dto.setCharacterId(like.getCharacter().getId());
                dto.setDate(like.getDate());
                dto.setId(like.getId());
                dto.setPostId(like.getPost().getId());
                return dto;
            }).toList();

            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> showLikesByPost(int postId) {
        try {
            List<Like> likes = this.likeRepo.findByPostId(postId);
            List<LikeDTO> dtos = likes.stream().map(like -> {
                LikeDTO dto = new LikeDTO();
                dto.setCharacterId(like.getCharacter().getId());
                dto.setDate(like.getDate());
                dto.setId(like.getId());
                dto.setPostId(postId);
                return dto;
            }).toList();

            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> delete(int characterId, int postId) {
        Optional<Like> likeOpt = this.likeRepo.findByCharacterIdAndPostId(characterId, postId);

        if (!likeOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Like like = likeOpt.get(); 

        try {
            likeRepo.delete(like);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<Boolean> existsByPostIdAndCharacterId(int characterId, int postId) {
        try {
            boolean liked = this.likeRepo.existsByPostIdAndCharacterId(postId, characterId);
            return ResponseEntity.ok(liked);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
