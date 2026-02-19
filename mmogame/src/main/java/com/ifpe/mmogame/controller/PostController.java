package com.ifpe.mmogame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ifpe.mmogame.dto.NewPostDTO;
import com.ifpe.mmogame.services.PostService;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postS;

    @PostMapping("/novo")
    public ResponseEntity<?> createNewPost(@RequestBody NewPostDTO pDto) {
        return this.postS.save(pDto);
    }

    @GetMapping("/{characterId}/perfil")
    public ResponseEntity<?> getAllPostsByCharacter(@PathVariable int characterId) {
        return this.postS.getPosts(characterId);
    }

    @GetMapping("/{characterId}")
    public ResponseEntity<?> getAllPosts(@PathVariable int characterId) {
        return this.postS.getOthersPosts(characterId);
    }

}
