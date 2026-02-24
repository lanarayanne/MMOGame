package com.ifpe.mmogame.services;

import java.util.List;

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
    }

    public ResponseEntity<?> showLikesByCharacter(int characterId) {
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
    }

    public ResponseEntity<?> showLikesByPost(int postId) {
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
    }

    public ResponseEntity<?> delete(int likeId) {
        if (!likeRepo.existsById(likeId)) {
            return ResponseEntity.notFound().build();
        }

        likeRepo.deleteById(likeId);
        return ResponseEntity.ok().build();
    }

}
