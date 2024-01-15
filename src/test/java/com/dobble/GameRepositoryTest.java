package com.dobble;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.dobble.model.Game;
import com.dobble.repositories.GameRepository;

@SpringBootTest
@AutoConfigureTestDatabase
public class GameRepositoryTest {

  @Autowired
  private GameRepository gameRepository;

  @Test
  void testRepositoryFindAll() {
    List<Game> games = gameRepository.findAll();
    assertNotNull(games);
  }

  @Test
  @Transactional
  void testFindByName() {
    String gameName = "exampleGame";
    Game game = new Game();
    game.setName(gameName);
    game.setMax_players(2);
    gameRepository.save(game);

    Optional<Game> foundGame = gameRepository.findByName(gameName);

    assertTrue(foundGame.isPresent());
    assertEquals(gameName, foundGame.get().getName());
  }

  @Test
  void testFindByNameNonExistent() {
    String nonExistentGameName = "nonExistentGame";
    Optional<Game> foundGame = gameRepository.findByName(nonExistentGameName);

    assertFalse(foundGame.isPresent());
  }

  @Test
  public void testFindGamesByPlayerId() {
    Integer playerId = 1;
    Optional<List<Game>> gamesByPlayerId = gameRepository.findGamesByPlayerId(playerId);
    assertTrue(gamesByPlayerId.isPresent());
    assertEquals(0, gamesByPlayerId.get().size());
  }

  @Test
  public void testFindGamesByPlayerUsername() {
    String username = "username";
    Optional<List<Game>> gamesByPlayerUsername = gameRepository.findGamesByPlayerUsername(username);
    assertTrue(gamesByPlayerUsername.isPresent());
    assertEquals(0, gamesByPlayerUsername.get().size());
  }
}
