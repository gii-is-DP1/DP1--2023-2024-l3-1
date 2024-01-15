package org.springframework.samples.petclinic.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.springframework.samples.petclinic.model.GamePlayer;
import org.springframework.samples.petclinic.model.Player;
import org.springframework.samples.petclinic.repositories.GamePlayerRepository;
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
  private GamePlayerRepository gamePlayerRepository;

  @Autowired
  private GameService gameService;

  @Autowired
  private GamePlayerService gamePlayerService;

  @Autowired
  PlayerService playerService;

  @Test
  @Transactional
  void testFindGame() {
    Game game = new Game();
    game.setName("Test Game");
    gameRepository.save(game);

    Optional<Game> foundGame = gameService.findGame(game.getId());

    assertTrue(foundGame.isPresent());
  }

  @Test
  void testFindGameNotFound() {
    Optional<Game> notFoundGame = gameService.findGame("nonexistent-id");
    assertFalse(notFoundGame.isPresent());
  }

  @Test
  @Transactional
  void testFindByNameGame() {
    Game game = new Game();
    game.setName("partida1");
    gameRepository.save(game);
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
  @Transactional
  void testGetAllGamesOfPlayer() {
    Player player = new Player();
    player.setUsername("testPlayer");
    player.setEmail("test@example.com");
    player.setPassword("password");
    playerRepository.save(player);

    Game game1 = new Game();
    Game game2 = new Game();
    game1.setName("game1");
    game2.setName("game2");
    gameRepository.save(game1);
    gameRepository.save(game2);

    gameService.addPlayerToGame(game1, player);
    gameService.addPlayerToGame(game2, player);

    List<Game> result = gameService.getAllGamesOfPlayer(player);

    assertEquals(2, result.size());
  }

  @Test
  @Transactional
  void testGetAllGamesOfPlayer_NoGames() {
    Player player = new Player();
    player.setUsername("testPlayer");
    player.setEmail("test@example.com");
    player.setPassword("password");
    playerRepository.save(player);

    List<Game> result = gameService.getAllGamesOfPlayer(player);

    assertEquals(0, result.size());
  }

  @Test
  @Transactional
  public void testGetCurrentGameOfPlayer() {
    Player player = new Player();
    player.setId(1);
    player.setUsername("player");
    player.setEmail("player@email.com");

    Game unfinishedGame = new Game();
    unfinishedGame.setName("Partida en curso");
    gameRepository.save(unfinishedGame);

    Game finishedGame = new Game();
    finishedGame.setName("Partida finalizada");
    finishedGame.setFinish(LocalDateTime.now());
    gameRepository.save(finishedGame);

    gameService.addPlayerToGame(unfinishedGame, player);

    Optional<Game> result = gameService.getCurrentGameOfPlayer(player);

    assertTrue(result.isPresent());
    assertEquals(unfinishedGame.getId(), result.get().getId());
  }

  @Test
  @Transactional
  void testGetGamesByPlayerUsername() {
    Player player = new Player();
    player.setUsername("testPlayer");
    player.setEmail("test@example.com");
    player.setPassword("password");
    playerRepository.save(player);

    Game game1 = new Game();
    Game game2 = new Game();
    game1.setName("game1");
    game2.setName("game2");
    gameRepository.save(game1);
    gameRepository.save(game2);

    gameService.addPlayerToGame(game1, player);
    gameService.addPlayerToGame(game2, player);

    List<Game> result = gameService.getGamesByPlayerUsername(player.getUsername());

    assertEquals(2, result.size());

  }

  @Test
  @Transactional
  void testGetGamesByPlayerUsername_NoGames() {
    Player player = new Player();
    player.setUsername("testPlayer");
    player.setEmail("test@example.com");
    player.setPassword("password");
    playerRepository.save(player);

    List<Game> result = gameService.getGamesByPlayerUsername(player.getUsername());

    assertEquals(0, result.size());
  }

  @Test
  @Transactional
  public void testGetCurrentGameByUsername() {
    Player player = new Player();
    player.setId(1);
    player.setUsername("player");
    player.setEmail("player@email.com");
    player.setPassword("1234");

    Game unfinishedGame = new Game();
    unfinishedGame.setName("Partida en curso");
    gameRepository.save(unfinishedGame);

    Game finishedGame = new Game();
    finishedGame.setName("Partida finalizada");
    finishedGame.setFinish(LocalDateTime.now());
    gameRepository.save(finishedGame);

    gameService.addPlayerToGame(unfinishedGame, player);
    GamePlayer gp = new GamePlayer();
    gp.setPlayer(player);
    gp.setGame(finishedGame);
    gamePlayerRepository.save(gp);
    player.setGame_players(List.of(gp));

    playerRepository.save(player);

    Optional<Game> result = gameService.getCurrentGameByUsername(player.getUsername());

    assertTrue(result.isPresent());
    assertEquals(unfinishedGame.getId(), result.get().getId());
  }

  @Test
  @Transactional
  @WithMockUser(username = "dobble", password = "dobble")
  public void testSaveGame() {
    Game game = new Game();
    Optional<Player> player = playerService.findCurrentPlayer();
    game.setName("Test Game");

    gameRepository.save(game);

    Optional<Game> foundGame = gameService.findGame(game.getId());

    assertTrue(foundGame.isPresent());
    assertEquals("Test Game", foundGame.get().getName());
  }

  @Test
  @Transactional
  void testUpdateGame() {
    Game initialGame = new Game();
    initialGame.setName("OldGameName");
    initialGame.setMax_players(5);
    gameRepository.save(initialGame);

    GameCreateDto payload = new GameCreateDto();
    payload.setName("NewGameName");
    payload.setMax_players(8);

    Game updatedGame = gameService.updateGame(payload, initialGame);

    assertEquals("NewGameName", updatedGame.getName());
    assertEquals(8, updatedGame.getMax_players());
  }

  @Test
  @Transactional
  void testFindAll() {
    Game game = new Game();
    game.setName("Test Game");
    gameRepository.save(game);

    Optional<List<Game>> allGames = gameService.findAll();
    assertTrue(allGames.isPresent());
    assertFalse(allGames.get().isEmpty());
  }

  @Test
  @Transactional
  void testCreateGame() {
    Player creator = new Player();
    creator.setUsername("creator");
    creator.setEmail("creator@example.com");
    creator.setPassword("password");
    playerRepository.save(creator);

    GameCreateDto payload = new GameCreateDto();
    payload.setName("NewGame");
    payload.setMax_players(8);

    Game createdGame = gameService.createGame(payload, creator);

    assertNotNull(createdGame.getId());
    assertEquals("NewGame", createdGame.getName());
    assertEquals(creator, createdGame.getCreator().getPlayer());
    assertEquals(8, createdGame.getMax_players());
  }

  @Test
  @Transactional
  void testRemovePlayerFromGame() {
    Player playerToRemove = new Player();
    playerToRemove.setUsername("playerToRemove");
    playerToRemove.setEmail("playerToRemove@example.com");
    playerToRemove.setPassword("password");
    playerRepository.save(playerToRemove);

    Game game = new Game();
    game.setName("TestGame");
    game.setMax_players(4);
    gameRepository.save(game);

    GamePlayer gamePlayer = new GamePlayer();
    gamePlayer.setPlayer(playerToRemove);
    gamePlayer.setGame(game);
    gamePlayerService.save(gamePlayer);

    game.setCreator(gamePlayer);
    gameRepository.save(game);

    game.getGame_players().add(gamePlayer);
    gameService.saveGame(game);

    gameService.removePlayerFromGame(game, playerToRemove);

    assertTrue(gameRepository.findById(game.getId()).isEmpty());
  }

  @Test
  @Transactional
  void testRemovePlayerFromFinishedGame() {
    Player playerToRemove = new Player();
    playerToRemove.setUsername("playerToRemove");
    playerToRemove.setEmail("playerToRemove@example.com");
    playerToRemove.setPassword("password");
    playerRepository.save(playerToRemove);

    Game game = new Game();
    game.setName("FinishedGame");
    game.setMax_players(4);
    game.setFinish(LocalDateTime.now());
    gameRepository.save(game);

    GamePlayer gamePlayer = new GamePlayer();
    gamePlayer.setPlayer(playerToRemove);
    gamePlayer.setGame(game);
    gamePlayerService.save(gamePlayer);

    game.getGame_players().add(gamePlayer);
    gameService.saveGame(game);

    gameService.removePlayerFromGame(game, playerToRemove);

    assertTrue(gameRepository.findById(game.getId()).isPresent());
    assertTrue(game.getGame_players().stream().anyMatch(gp -> gp.getPlayerId() == playerToRemove.getId()));
  }

  @Test
  @Transactional
  void testAddPlayerToGame() {
    Player playerToAdd = new Player();
    playerToAdd.setUsername("playerToAdd");
    playerToAdd.setEmail("playerToAdd@example.com");
    playerToAdd.setPassword("password");
    playerRepository.save(playerToAdd);

    Game game = new Game();
    game.setName("TestGame");
    game.setMax_players(4);
    gameRepository.save(game);

    gameService.addPlayerToGame(game, playerToAdd);

    assertTrue(game.getGame_players().stream().anyMatch(gp -> gp.getPlayerId() == playerToAdd.getId()));

    Game savedGame = gameRepository.findById(game.getId()).orElse(null);
    assertNotNull(savedGame);
    assertTrue(savedGame.getGame_players().stream().anyMatch(gp -> gp.getPlayerId() == playerToAdd.getId()));
  }
}
