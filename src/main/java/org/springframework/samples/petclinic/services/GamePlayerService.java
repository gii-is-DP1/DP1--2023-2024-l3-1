package org.springframework.samples.petclinic.services;

import java.util.Optional;

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

  public GamePlayerService(GameService gameService,PlayerService playerService,GamePlayerRepository gamePlayerRepository){
    this.gameService= gameService;
    this.playerService = playerService; 
    this.gamePlayerRepository = gamePlayerRepository; 
  }

  @Transactional()
  public GamePlayer saveGamePlayer(@Valid GamePlayer gamePlayer){
    this.gamePlayerRepository.save(gamePlayer); 
    return gamePlayer; 
  }

  @Transactional(readOnly = true)
  public GamePlayer addPlayerToGame(String gameId, Integer playerId){
    Game gameToJoin= gameService.findGame(gameId); 
    Optional<Player> playerToJoin= playerService.findPlayer(playerId); 

    GamePlayer gamePlayer= new GamePlayer(playerToJoin.get(),gameToJoin); 
    saveGamePlayer(gamePlayer);
    return gamePlayer;
  }
}
