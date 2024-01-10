package org.springframework.samples.petclinic.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.dto.GameCreateDto;
import org.springframework.samples.petclinic.model.Game;
import org.springframework.samples.petclinic.model.Player;
import org.springframework.samples.petclinic.repositories.GameRepository;
import org.springframework.samples.petclinic.repositories.PlayerRepository;
import org.springframework.security.test.context.support.WithMockUser;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase
public class GameServiceTest {
  @Autowired
  private GameRepository gameRepository;

  @Autowired
  private PlayerRepository playerRepository;

  @Autowired
  private GameService gameService;

  @Autowired
  PlayerService playerService;

  @Test
  void testFindAll() {
    Optional<List<Game>> allGames = gameService.findAll();
    assertTrue(allGames.isPresent());
    assertFalse(allGames.get().isEmpty());
  }

  @Test
  void testFindByNameGame() {
    Optional<Game> foundGame = gameService.findByNameGame("partida1");
    assertTrue(foundGame.isPresent());
    assertEquals("partida1", foundGame.get().getName());
  }

  @Test
  void testFindByNameNotFoundGame() {
    Optional<Game> foundGame = gameService.findByNameGame("partida5");
    assertFalse(foundGame.isPresent());
  }

  @Test
  void testFindGameById() {
    Optional<Game> foundGame = gameService.findGame("123e4567-e89b-12d3-a456-426655440000");
    assertTrue(foundGame.isPresent());
    assertEquals("partida1", foundGame.get().getName());
  }

  @Test
  void testFindGameByIdNotFound() {
    Optional<Game> notFoundGame = gameService.findGame("nonexistent-id");
    assertFalse(notFoundGame.isPresent());
  }

  @Test
  void testIsGameInLobby() {
    Optional<Game> foundGame = gameService.findGame("123e4567-e89b-12d3-a456-324833943923");
    assertTrue(foundGame.isPresent());
    assertTrue(foundGame.get().isOnLobby());
  }

  @Test
  void testIsGameNotInLobby() {
    Optional<Game> foundGame = gameService.findGame("123e4567-e89b-12d3-a456-324833943924");
    assertTrue(foundGame.isPresent());
    assertFalse(foundGame.get().isOnLobby());
  }

  @Test
  void testIsGameOngoing() {
    Optional<Game> foundGame = gameService.findGame("123e4567-e89b-12d3-a456-324833943924");
    assertTrue(foundGame.isPresent());
    assertTrue(foundGame.get().isOngoing());
  }

  @Test
  void testIsGameFinished() {
    Optional<Game> foundGame = gameService.findGame("123e4567-e89b-12d3-a456-426655440000");
    assertTrue(foundGame.isPresent());
    assertTrue(foundGame.get().isFinished());
  }

  @Test
  @Transactional
  @WithMockUser(username = "dobble", password = "dobble")
  public void testSaveGame() {
    Game game = new Game();
    Optional<Player> player = playerService.findCurrentPlayer();
    game.setName("Test Game");
    game.setRaw_creator(player.get());
    game.setRaw_players(List.of(player.get()));
    gameRepository.save(game);

    Optional<Game> foundGame = gameService.findGame(game.getId());

    assertTrue(foundGame.isPresent());
    assertEquals("Test Game", foundGame.get().getName());
  }

  @Transactional
  @Test
  void testUpdateGame() {
    Optional<Game> gameToUpdate = gameService.findGame("123e4567-e89b-12d3-a456-426655440000");
    assertTrue(gameToUpdate.isPresent());

    GameCreateDto gameCreateDto = new GameCreateDto();
    gameCreateDto.setName("NombreActualizado");
    gameCreateDto.setMax_players(7);

    gameService.updateGame(gameCreateDto, "123e4567-e89b-12d3-a456-426655440000");

    Optional<Game> updatedGame = gameService.findGame("123e4567-e89b-12d3-a456-426655440000");

    assertTrue(updatedGame.isPresent());
    assertEquals("NombreActualizado", updatedGame.get().getName());
    assertEquals(7, updatedGame.get().getMax_players());

  }

  @Transactional
  @Test
  void testUpdateGameNotFound() {
    GameCreateDto gameCreateDto = new GameCreateDto();
    gameCreateDto.setName("NombreActualizado");
    gameCreateDto.setMax_players(7);

    assertEquals(Optional.empty(), gameService.updateGame(gameCreateDto, "nonexistent-id"));
  }

  @Test
  @Transactional
  void testAddPlayerToGame() {
    Game testGame = new Game();
    testGame.setName("Test Game");
    testGame.setMax_players(3);
    testGame.setStart(null);
    gameRepository.save(testGame);

    Player testPlayer = new Player();
    testPlayer.setUsername("TestPlayer");

    Optional<Game> resultGameOptional = gameService.addPlayerToGame(testGame.getId(), testPlayer);

    assertTrue(resultGameOptional.isPresent());
    Game resultGame = resultGameOptional.get();
    assertEquals(testGame.getId(), resultGame.getId());
    assertTrue(resultGame.getRaw_game_players().size() > 0);

    boolean playerFoundInGame = resultGame.getRaw_game_players().stream()
        .anyMatch(gp -> gp.getPlayer().getUsername().equals(testPlayer.getUsername()));
    assertTrue(playerFoundInGame);
  }

  @Test
  @Transactional
  void testAddPlayerToGamePlayerAlreadyInGame() {
    Game testGame = new Game();
    testGame.setName("Test Game");
    testGame.setMax_players(3);
    testGame.setStart(null);
    gameRepository.save(testGame);

    Player testPlayer = new Player();
    testPlayer.setUsername("TestPlayer");

    gameService.addPlayerToGame(testGame.getId(), testPlayer);

    assertThrows(RuntimeException.class, () -> {
      gameService.addPlayerToGame(testGame.getId(), testPlayer);
    });
  }

  @Test
  @Transactional
  void testAddPlayerToGameGameNotOnLobby() {
    Game testGame = new Game();
    testGame.setName("Test Game");
    testGame.setMax_players(3);
    testGame.setStart(LocalDateTime.now());
    gameRepository.save(testGame);

    Player testPlayer = new Player();
    testPlayer.setUsername("TestPlayer");

    Optional<Game> resultGameOptional = gameService.addPlayerToGame(testGame.getId(), testPlayer);

    assertFalse(resultGameOptional.isPresent());
  }
}
