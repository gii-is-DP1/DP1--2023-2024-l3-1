package org.springframework.samples.petclinic.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dto.GameCreateDto;
import org.springframework.samples.petclinic.model.Game;
import org.springframework.samples.petclinic.repositories.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class GameService {
    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Game> findGame(String id) {
        return gameRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Game> findByNameGame(String name) {
        return gameRepository.findByName(name);
    }

    @Transactional()
    public Game saveGame(@Valid Game game) {
        this.gameRepository.save(game);
        return game;
    }

    @Transactional(rollbackFor = Exception.class)
    public Optional<Game> updateGame(@Valid GameCreateDto payload, String idToUpdate) {
        Optional<Game> optionalGameToUpdate = findGame(idToUpdate);

        if (optionalGameToUpdate.isPresent()) {
            Game gameToUpdate = optionalGameToUpdate.get();
            String newGameName = payload.getName();
            Integer newGameMaxPlayers = payload.getMaxPlayers();

            if (newGameName != null && !newGameName.isBlank()) {
                gameToUpdate.setName(newGameName);
            }
            if (newGameMaxPlayers != null) {
                gameToUpdate.setMaxPlayers(newGameMaxPlayers);
            }
            this.gameRepository.save(gameToUpdate);
        }

        return optionalGameToUpdate;
    }

    public Optional<List<Game>> findAll() {
        List<Game> games = gameRepository.findAll();
        return Optional.ofNullable(games);
    }
}
