package com.ifpe.mmogame.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.ifpe.mmogame.dto.NewPostDTO;
import com.ifpe.mmogame.dto.PostDTO;
import com.ifpe.mmogame.entities.Post;
import com.ifpe.mmogame.entities.Photo;
import com.ifpe.mmogame.entities.User;
import com.ifpe.mmogame.entities.Character;
import com.ifpe.mmogame.repositories.CharacterRepository;
import com.ifpe.mmogame.repositories.PhotoRepository;
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
    @Autowired
    private PhotoRepository photoRepo;

    public ResponseEntity<?> save(NewPostDTO pDto) {

        try {
            if (pDto.getText().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            User u = this.userRepo.findByEmail(this.jwtUtils.getAuthorizedId()).get();
            Character c = this.characterRepo.findByIdAndUserId(pDto.getCharacterId(), u.getId()).get();

            Post p = new Post();
            p.setText(pDto.getText());
            p.setCharacter(c);
            p = this.postRepo.save(p);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    public ResponseEntity<?> getOthersPosts(int characterId) {
        try {
            List<Post> posts = this.postRepo.findOtherCharactersPosts(characterId);

            List<PostDTO> dtos = posts.stream().map(post -> {
                PostDTO dto = new PostDTO();
                dto.setId(post.getId());
                dto.setDate(post.getDate());
                dto.setText(post.getText());
                Character author = post.getCharacter();

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
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> getPosts(int characterId) {
        try {
            List<Post> posts = this.postRepo.findByCharacterIdOrderByDateDesc(characterId);
            List<PostDTO> dtos = posts.stream().map(post -> {
                PostDTO dto = new PostDTO();
                dto.setId(post.getId());
                dto.setDate(post.getDate());
                dto.setText(post.getText());
                Character author = post.getCharacter();

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
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> delete(int postId) {
        try {
            this.postRepo.deleteById(postId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // public ResponseEntity<?> update(Integer postId, NewPostDTO pDto) {

    // User u = this.userRepo.findByEmail(this.jwtUtils.getAuthorizedId()).get();

    // Post p = this.postRepo.findById(postId).get();
    // p.setText(pDto.getText());
    // this.postRepo.save(p);
    // return ResponseEntity.ok().build();
    // }

}
