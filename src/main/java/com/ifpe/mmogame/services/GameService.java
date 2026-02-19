package com.ifpe.mmogame.services;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.ifpe.mmogame.entities.Game;
import com.ifpe.mmogame.repositories.GameRepository;

@Component
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String API_URL = "https://www.mmobomb.com/api1/games";

    public List<Game> syncGames() {
        Game[] gamesArray = restTemplate.getForObject(API_URL, Game[].class);
        if (gamesArray != null) {
            List<Game> gamesList = Arrays.asList(gamesArray);

            return gameRepository.saveAll(gamesList);
        }
        return List.of();
    }

    public List<Game> listAllFromDb() {
        return gameRepository.findAll();
    }

}
