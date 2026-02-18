package com.ifpe.mmogame.services;

import java.util.List;

import org.aspectj.weaver.patterns.TypePatternQuestions.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ifpe.mmogame.dto.NewPostDTO;
import com.ifpe.mmogame.dto.PostDTO;
import com.ifpe.mmogame.entities.Post;
import com.ifpe.mmogame.entities.User;
import com.ifpe.mmogame.entities.Character;
import com.ifpe.mmogame.repositories.CharacterRepository;
import com.ifpe.mmogame.repositories.PostRepository;
import com.ifpe.mmogame.repositories.UserRepository;
import com.ifpe.mmogame.security.JwtUtils;

@Component
public class PostService {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private CharacterRepository characterRepo;
    @Autowired
    private UserRepository userRepo;

    public ResponseEntity<?> save(String text, int characterId) {

        if (text.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        User u = this.userRepo.findByEmail(this.jwtUtils.getAuthorizedId()).get();

        Character c = this.characterRepo.findByIdAndUserId(characterId, u.getId()).get();

        // Character c = this.characterRepo.findById(characterId).get();

        Post p = new Post();
        p.setText(text);
        p.setCharacter(c);
        p = this.postRepo.save(p);

        return ResponseEntity.ok().build();

    }

    public ResponseEntity<?> update(Integer postId, NewPostDTO pDto) {

        User u = this.userRepo.findByEmail(this.jwtUtils.getAuthorizedId()).get();

        Post p = this.postRepo.findById(postId).get();
        p.setText(pDto.getText());
        this.postRepo.save(p);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> getOthersPosts(int characterId) {
        List<Post> posts = this.postRepo.findOtherCharactersPosts(characterId);

        List<PostDTO> dtos = posts.stream().map(post -> {
            PostDTO dto = new PostDTO();
            dto.setId(post.getId());
            dto.setDate(post.getDate());
            dto.setText(post.getText());
            dto.setCharacterId(post.getCharacter().getId());
            return dto;
        }).toList();

        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<?> getPosts(int characterId) {
        List<Post> posts = this.postRepo.findByCharacterId(characterId);
        List<PostDTO> dtos = posts.stream().map(post -> {
            PostDTO dto = new PostDTO();
            dto.setId(post.getId());
            dto.setDate(post.getDate());
            dto.setText(post.getText());
            dto.setCharacterId(post.getCharacter().getId());
            return dto;
        }).toList();

        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<?> delete(int postId) {

        this.postRepo.deleteById(postId);
        return ResponseEntity.ok().build();
    }

}
