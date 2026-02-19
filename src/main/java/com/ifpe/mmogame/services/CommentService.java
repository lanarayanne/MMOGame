package com.ifpe.mmogame.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ifpe.mmogame.dto.CommentPostDTO;
import com.ifpe.mmogame.dto.NewCommentDTO;
import com.ifpe.mmogame.entities.Post;
import com.ifpe.mmogame.entities.Character;
import com.ifpe.mmogame.entities.CommentPost;
import com.ifpe.mmogame.entities.User;
import com.ifpe.mmogame.repositories.CharacterRepository;
import com.ifpe.mmogame.repositories.CommentRepositoy;
import com.ifpe.mmogame.repositories.PostRepository;
import com.ifpe.mmogame.repositories.UserRepository;
import com.ifpe.mmogame.security.JwtUtils;

@Component
public class CommentService {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private CharacterRepository characterRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CommentRepositoy commentRepo;

    public ResponseEntity<?> save(NewCommentDTO cDto) {

        if (cDto.getText().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        User u = this.userRepo.findByEmail(this.jwtUtils.getAuthorizedId()).get();
        Character c = this.characterRepo.findByIdAndUserId(cDto.getCharacterId(), u.getId()).get();
        Post p = this.postRepo.findById(cDto.getPostId()).get();

        CommentPost comment = new CommentPost();
        comment.setCharacter(c);
        comment.setPost(p);
        comment.setText(cDto.getText());
        comment = this.commentRepo.save(comment);

        return ResponseEntity.ok().build();

    }

    public ResponseEntity<?> getCommentsByPostId(int postId) {
        List<CommentPost> comments = this.commentRepo.findByPostId(postId);
        List<CommentPostDTO> dtos = comments.stream().map(comment -> {
            CommentPostDTO dto = new CommentPostDTO();
            dto.setCharacterId(comment.getCharacter().getId());
            dto.setDate(comment.getDate());
            dto.setId(comment.getId());
            dto.setPostId(postId);
            dto.setText(comment.getText());
            return dto;
        }).toList();

        
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<?> getCommentsByCharacter(int characterId) {
        User u = this.userRepo.findByEmail(this.jwtUtils.getAuthorizedId()).get();
        Character c = this.characterRepo.findByIdAndUserId(characterId, u.getId()).get();
        List<CommentPost> comments = this.commentRepo.findByCharacterId(c.getId());
        List<CommentPostDTO> dtos = comments.stream().map(comment -> {
            CommentPostDTO dto = new CommentPostDTO();
            dto.setCharacterId(comment.getCharacter().getId());
            dto.setDate(comment.getDate());
            dto.setId(comment.getId());
            dto.setPostId(comment.getPost().getId());
            dto.setText(comment.getText());
            return dto;
        }).toList();

        
        return ResponseEntity.ok(dtos);
    }



}
