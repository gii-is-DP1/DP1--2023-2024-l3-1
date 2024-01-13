package org.springframework.samples.petclinic.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Game;
import org.springframework.samples.petclinic.model.GamePlayer;
import org.springframework.samples.petclinic.model.Player;
import org.springframework.samples.petclinic.repositories.GamePlayerRepository;
import org.springframework.samples.petclinic.repositories.GameRepository;
import org.springframework.samples.petclinic.repositories.HandRepository;
import org.springframework.samples.petclinic.repositories.PlayerRepository;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase
public class GamePlayerRepositoryTest {

  @Autowired
  private GamePlayerRepository gamePlayerRepository;

  @Autowired
  private PlayerRepository playerRepository;

  @Autowired
  private GameRepository gameRepository;

  @Autowired
  private HandRepository handRepository;

  @Test
  @Transactional
  void testFindGamePlayerByUsername() {
    String username = "exampleUsername";
    GamePlayer gamePlayer = new GamePlayer();

    Player player = new Player();
    player.setUsername(username);
    player.setEmail("example@email.com");
    player.setPassword("password");
    playerRepository.save(player);

    Game game = new Game();
    game.setName("game");
    game.setMax_players(2);
    gameRepository.save(game);

    gamePlayer.setPlayer(player);
    gamePlayer.setGame(game);
    gamePlayerRepository.save(gamePlayer);

    Optional<List<GamePlayer>> foundGamePlayers = gamePlayerRepository.findGamePlayerByUsername(username);

    assertTrue(foundGamePlayers.isPresent());
    assertEquals(1, foundGamePlayers.get().size());
    assertEquals(username, foundGamePlayers.get().get(0).getPlayer().getUsername());
  }

}
