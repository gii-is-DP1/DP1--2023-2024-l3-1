package org.springframework.samples.petclinic.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dto.GameCreateDto;
import org.springframework.samples.petclinic.model.Game;
import org.springframework.samples.petclinic.model.GamePlayer;
import org.springframework.samples.petclinic.model.Player;
import org.springframework.samples.petclinic.repositories.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.security.auth.message.AuthException;
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
            Integer newGameMaxPlayers = payload.getMax_players();

            if (newGameName != null && !newGameName.isBlank()) {
                gameToUpdate.setName(newGameName);
            }
            if (newGameMaxPlayers != null) {
                gameToUpdate.setMax_players(newGameMaxPlayers);
            }
            this.gameRepository.save(gameToUpdate);
        }

        return optionalGameToUpdate;
    }

    @Transactional(readOnly = true)
    public Optional<List<Game>> findAll() {
        List<Game> games = gameRepository.findAll();
        return Optional.ofNullable(games);
    }


    @Transactional
    public Optional<Game> addPlayerToGame(String gameId, Player player) throws AuthException {
        Optional<Game> optionalGame = gameRepository.findById(gameId);

        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            Integer currentPlayer = 1;

            
            if (game.getRaw_game_players() == null) {
                game.setRaw_game_players(new ArrayList<>());
            }
            
            if (game.isOnLobby() && game.getRaw_game_players().size() + currentPlayer <= game.getMaxPlayers()) {
                GamePlayer gamePlayer = new GamePlayer();
                gamePlayer.setGame(game);
                gamePlayer.setPlayer(player);
                game.getRaw_game_players().add(gamePlayer);

                //saveGame(game);
                return Optional.of(game);
            }
        }else{
            throw new AuthException("El código introducido no es correcto"+ gameId); 
        }

        return Optional.empty();
    }
}
