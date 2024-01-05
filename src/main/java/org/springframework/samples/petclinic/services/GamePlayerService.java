package org.springframework.samples.petclinic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.GamePlayer;
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

  @Transactional(readOnly = true)
  public Iterable<GamePlayer> findAll() {
    return gamePlayerRepository.findAll();
  }

}
