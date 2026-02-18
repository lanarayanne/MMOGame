package com.ifpe.mmogame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifpe.mmogame.dto.NewCommentDTO;
import com.ifpe.mmogame.services.CommentService;
import com.ifpe.mmogame.services.PostService;

@RestController
@RequestMapping("/comentario")
public class CommentController {
    @Autowired
    private CommentService commentS;

    @PostMapping("/novo")
    public ResponseEntity<?> createNewPost(@RequestBody NewCommentDTO cDto) {
        return this.commentS.save(cDto);
    }

    @GetMapping("post/{postId}")
    public ResponseEntity<?> getCommentsByPostId(@PathVariable int postId) {
        return this.commentS.getCommentsByPostId(postId);
    }

    @GetMapping("personagem/{characterId}")
    public ResponseEntity<?> getCommentsByCharacterId(@PathVariable int characterId) {
        return this.commentS.getCommentsByCharacter(characterId);
    }


}
