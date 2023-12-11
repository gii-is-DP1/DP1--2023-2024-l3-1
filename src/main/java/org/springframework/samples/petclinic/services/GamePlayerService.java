package org.springframework.samples.petclinic.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dto.PublicPlayerDto;
import org.springframework.samples.petclinic.model.Game;
import org.springframework.samples.petclinic.model.GamePlayer;
import org.springframework.samples.petclinic.model.Player;
import org.springframework.samples.petclinic.repositories.GamePlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class GamePlayerService {
  private final GameService gameService;
  private final PlayerService playerService;
  private final GamePlayerRepository gamePlayerRepository;

  @Autowired
  public GamePlayerService(GameService gameService, PlayerService playerService,
      GamePlayerRepository gamePlayerRepository) {
    this.gameService = gameService;
    this.playerService = playerService;
    this.gamePlayerRepository = gamePlayerRepository;
  }

  @Transactional()
  public GamePlayer saveGamePlayer(@Valid GamePlayer gamePlayer) {
    this.gamePlayerRepository.save(gamePlayer);
    return gamePlayer;
  }

  @Transactional()
  public void addPlayerToGame(String gameId, Integer playerId) {
    Optional<Game> gameToJoinOpt = gameService.findGame(gameId);
    Optional<Player> playerToJoinOpt = playerService.findPlayer(playerId);

    if (gameToJoinOpt.isPresent() && playerToJoinOpt.isPresent()) {

      
        Game gameToJoin = gameToJoinOpt.get();

        if (gameToJoin.isFull()) {
          throw new IllegalStateException("El juego está lleno y no se pueden unir más jugadores.");
        }
        if (gameToJoin.getPlayers().contains(new PublicPlayerDto(playerToJoinOpt.get()))) {
          throw new IllegalStateException("El jugador ya está en la partida.");
        }

        GamePlayer gamePlayer = new GamePlayer();
        gamePlayer.setGame(gameToJoin);
        gamePlayer.setPlayer(playerToJoinOpt.get());
        saveGamePlayer(gamePlayer);


    }

  }
}
