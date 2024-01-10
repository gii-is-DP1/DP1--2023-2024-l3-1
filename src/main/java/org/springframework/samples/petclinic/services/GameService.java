package org.springframework.samples.petclinic.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dto.GameCreateDto;
import org.springframework.samples.petclinic.model.Card;
import org.springframework.samples.petclinic.model.Figure;
import org.springframework.samples.petclinic.model.Game;
import org.springframework.samples.petclinic.model.GamePlayer;
import org.springframework.samples.petclinic.model.Hand;
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

                // saveGame(game);
                return Optional.of(game);
            }
        } else {
            throw new AuthException("El código introducido no es correcto" + gameId);
        }

        return Optional.empty();
    }

    @Transactional
    public void playFigure(String gameId, Integer playerId, Integer figureId) {
        Optional<Game> optionalGame = findGame(gameId);
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();

            Optional<GamePlayer> optionalGamePlayer = game.getRaw_game_players()
                    .stream()
                    .filter(gp -> gp.getPlayerId().equals(playerId))
                    .findFirst();

            if (optionalGamePlayer.isPresent()) {
                GamePlayer gamePlayer = optionalGamePlayer.get();
                Hand playerHand = gamePlayer.getHand();
                Card currentCard = playerHand.getCurrentCard();

                Figure selectedFigure = currentCard.getFigures().stream()
                        .filter(figure -> figure.getId().equals(figureId))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException(
                                "Figura seleccionada no encontrada en la mano del jugador."));

                Card centralCard = game.getCentralCard();
                Figure matchingFigure = currentCard.getMatchingIcon(centralCard);

                if (selectedFigure.equals(matchingFigure)){
                    game.setCentralCard(currentCard);
                    if(playerHand.isLastCard()){
                       game.setFinish(LocalDateTime.now());
                    } else {
                        playerHand.getNextCard();
                    }

                    this.saveGame(game);

                } else {
                throw new RuntimeException("La figura seleccionada no coincide con la carta central.");
            }

            } else {
                throw new RuntimeException("Jugador no encontrado en el juego.");
            }
        } else {
            throw new RuntimeException("Juego no encontrado.");
        }

    }

}
