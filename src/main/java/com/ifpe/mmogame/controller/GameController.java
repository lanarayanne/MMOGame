package com.ifpe.mmogame.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifpe.mmogame.entities.Game;
import com.ifpe.mmogame.services.GameService;

@RestController
@RequestMapping("/api/game")
public class GameController {
    @Autowired
    private GameService gameService;

    @GetMapping("/games")
    public ResponseEntity<List<Game>> listGames() {
        return ResponseEntity.ok(gameService.getAllGames());
    }
    
}
