package com.ifpe.mmogame.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ifpe.mmogame.entities.Game;


@Component
public class GameService {
    private final RestTemplate restTemplate;
    private final String API_URL = "https://www.mmobomb.com/api/games";

    public GameService() {
        this.restTemplate = new RestTemplate();
    }

    public List<Game> getAllGames() {
        Game[] games = restTemplate.getForObject(API_URL, Game[].class);
        return Arrays.asList(games);
    }

}
