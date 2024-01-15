package com.dobble.services;

import java.util.Optional;

import com.dobble.model.Game;
import com.dobble.model.GamePlayer;
import com.dobble.model.Player;
import com.dobble.repositories.GamePlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class GamePlayerService {
  private final GamePlayerRepository gamePlayerRepository;

  @Autowired
  public GamePlayerService(GamePlayerRepository gamePlayerRepository) {
    this.gamePlayerRepository = gamePlayerRepository;
  }

  @Transactional
  public GamePlayer save(@Valid GamePlayer gamePlayer) {
    return this.gamePlayerRepository.save(gamePlayer);
  }

  @Transactional(readOnly = true)
  private Iterable<GamePlayer> findAll() {
    return gamePlayerRepository.findAll();
  }

  public GamePlayer createGamePlayerCreator(Player player) {
    GamePlayer game_player = new GamePlayer();
    game_player.setPlayer(player);

    return game_player;
  }

  @Transactional
  public GamePlayer createGamePlayer(Player player, Game game) {
    GamePlayer game_player = new GamePlayer();
    game_player.setPlayer(player);
    game_player.setGame(game);
    this.save(game_player);

    return game_player;
  }

  @Transactional(readOnly = true)
  public Optional<GamePlayer> getGamePlayerByUsernameAndGame(String username, Game game) {
    return this.gamePlayerRepository.findGamePlayerByUsernameAndGame(username, game.getId());
  }
}
