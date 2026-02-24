package com.ifpe.mmogame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ifpe.mmogame.dto.NewLikeDTO;
import com.ifpe.mmogame.services.LikeService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/like")
public class LikeController {
    @Autowired
    private LikeService likeS;

    @PostMapping("/novo")
    public ResponseEntity<?> newLike(@RequestBody NewLikeDTO lDto) {
        return this.likeS.save(lDto);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getLikeByPost(@PathVariable int postId) {
        return this.likeS.showLikesByPost(postId);
    }

    @GetMapping("/personagem/{characterId}")
    public ResponseEntity<?> getLikeByCharacter(@PathVariable int characterId) {
        return this.likeS.showLikesByCharacter(characterId);
    }

    @DeleteMapping("/{likeId}")
    public ResponseEntity<?> deleteLike(@PathVariable int likeId) {
        return this.likeS.delete(likeId);
    }

}
