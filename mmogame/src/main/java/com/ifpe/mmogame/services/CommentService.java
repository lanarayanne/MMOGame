package com.ifpe.mmogame.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.ifpe.mmogame.dto.CommentPostDTO;
import com.ifpe.mmogame.dto.NewCommentDTO;
import com.ifpe.mmogame.entities.Post;
import com.ifpe.mmogame.entities.Character;
import com.ifpe.mmogame.entities.CommentPost;
import com.ifpe.mmogame.entities.Photo;
import com.ifpe.mmogame.entities.User;
import com.ifpe.mmogame.repositories.CharacterRepository;
import com.ifpe.mmogame.repositories.CommentRepositoy;
import com.ifpe.mmogame.repositories.PhotoRepository;
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
    @Autowired
    private PhotoRepository photoRepo;

    public ResponseEntity<?> save(NewCommentDTO cDto) {

        try {
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
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    public ResponseEntity<?> getCommentsByPostId(int postId) {
        List<CommentPost> comments = this.commentRepo.findByPostId(postId);
        List<CommentPostDTO> dtos = comments.stream().map(comment -> {
            CommentPostDTO dto = new CommentPostDTO();
            dto.setDate(comment.getDate());
            dto.setId(comment.getId());
            dto.setPostId(postId);
            dto.setText(comment.getText());

            Character author = this.characterRepo.findById(comment.getCharacter().getId()).get();
            dto.setCharacterId(author.getId());
            dto.setName(author.getName());
            dto.setUniqueName(author.getUniqueName());
            
            Optional<Photo> optPhoto = this.photoRepo.findByCharacterId(author.getId());

            if (optPhoto.isPresent()) {
                dto.setPhotoContent(optPhoto.get().getContent());
                dto.setPhotoExtension(optPhoto.get().getExtension());
            }
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
            dto.setDate(comment.getDate());
            dto.setId(comment.getId());
            dto.setPostId(comment.getPost().getId());
            dto.setText(comment.getText());

            Character author = this.characterRepo.findById(comment.getCharacter().getId()).get();
            dto.setCharacterId(author.getId());
            dto.setName(author.getName());
            dto.setUniqueName(author.getUniqueName());
            
            Optional<Photo> optPhoto = this.photoRepo.findByCharacterId(author.getId());

            if (optPhoto.isPresent()) {
                dto.setPhotoContent(optPhoto.get().getContent());
                dto.setPhotoExtension(optPhoto.get().getExtension());
            }
            return dto;
        }).toList();

        return ResponseEntity.ok(dtos);
    }

}
