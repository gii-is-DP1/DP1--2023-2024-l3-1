package org.springframework.samples.petclinic.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.dto.GameCreateDto;
import org.springframework.samples.petclinic.model.Game;
import org.springframework.samples.petclinic.model.GamePlayer;
import org.springframework.samples.petclinic.model.Player;
import org.springframework.samples.petclinic.repositories.GameRepository;
import org.springframework.samples.petclinic.repositories.PlayerRepository;
import org.springframework.security.test.context.support.WithMockUser;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase
public class GameServiceTest {
  @Mock
  private GameRepository gameRepository;

  @Autowired
  private PlayerRepository playerRepository;

  @InjectMocks
  private GameService gameService;

  @Autowired
  PlayerService playerService;

  @Autowired
  CardService cardService;

  @Mock
  private GamePlayerService gamePlayerService;

  @Test
  void testFindGame() {
    String gameId = "123";
    Game expectedGame = new Game();

    Mockito.when(gameRepository.findById(gameId)).thenReturn(Optional.of(expectedGame));

    Optional<Game> result = gameService.findGame(gameId);
    assertAll(
        () -> assertTrue(result.isPresent(), "Result should be present"),
        () -> assertEquals(expectedGame, result.get(), "Result should match the expected game"));

    Mockito.verify(gameRepository, Mockito.times(1)).findById(gameId);
  }

  @Test
  void testFindGameNotFound() {
    String gameId = "nonexistent_id";

    when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

    Optional<Game> result = gameService.findGame(gameId);

    assertFalse(result.isPresent(), "Result should be empty");

    verify(gameRepository, times(1)).findById(gameId);
  }

  @Test
  void testFindByNameGame() {
    String gameName = "Game";
    Game expectedGame = new Game();

    when(gameRepository.findByName(gameName)).thenReturn(Optional.of(expectedGame));

    Optional<Game> result = gameService.findByNameGame(gameName);

    assertAll(
        () -> assertTrue(result.isPresent(), "Result should be present"),
        () -> assertEquals(expectedGame, result.get(), "Result should match the expected game"));

    verify(gameRepository, times(1)).findByName(gameName);
  }

  @Test
  void testFindByNameGameNotFound() {
    String gameName = "NonexistentGame";

    when(gameRepository.findByName(gameName)).thenReturn(Optional.empty());

    Optional<Game> result = gameService.findByNameGame(gameName);

    assertFalse(result.isPresent(), "Result should be empty");

    verify(gameRepository, times(1)).findByName(gameName);
  }

  @Test
  void testFindAll() {
    Game game1 = new Game();
    Game game2 = new Game();
    List<Game> expectedGames = Arrays.asList(game1, game2);

    when(gameRepository.findAll()).thenReturn(expectedGames);

    Optional<List<Game>> result = gameService.findAll();

    assertAll(
        () -> assertTrue(result.isPresent(), "Result should be present"),
        () -> assertEquals(expectedGames, result.get(), "Result should match the expected games"));

    verify(gameRepository, times(1)).findAll();
  }

  @Test
  void testFindByNameNotFoundGame() {
    Optional<Game> foundGame = gameService.findByNameGame("partida5");
    assertFalse(foundGame.isPresent());
  }

  @Test
  void testGetAllGamesOfPlayer() {
    Player player = new Player();
    Game game1 = new Game();
    Game game2 = new Game();
    List<Game> expectedGames = Arrays.asList(game1, game2);

    when(gameRepository.findGamesByPlayerId(player.getId())).thenReturn(Optional.of(expectedGames));

    List<Game> result = gameService.getAllGamesOfPlayer(player);

    assertEquals(expectedGames, result, "Result should match the expected games");

    verify(gameRepository, times(1)).findGamesByPlayerId(player.getId());
  }

  @Test
  void testGetAllGamesOfPlayerEmpty() {
    Player player = new Player();

    when(gameRepository.findGamesByPlayerId(player.getId())).thenReturn(Optional.empty());

    List<Game> result = gameService.getAllGamesOfPlayer(player);

    assertTrue(result.isEmpty(), "Result should be an empty list");

    verify(gameRepository, times(1)).findGamesByPlayerId(player.getId());
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
    // game.setRaw_creator(player.get());
    // game.setRaw_players(List.of(player.get()));
    gameRepository.save(game);

    Optional<Game> foundGame = gameService.findGame(game.getId());

    assertTrue(foundGame.isPresent());
    assertEquals("Test Game", foundGame.get().getName());
  }

  @Test
  void testUpdateGame() {
    String existingGameName = "Existing Game";
    Integer existingMaxPlayers = 4;

    Game existingGame = new Game();
    existingGame.setName(existingGameName);
    existingGame.setMax_players(existingMaxPlayers);

    GameCreateDto updatePayload = new GameCreateDto();
    updatePayload.setName("Updated Game");
    updatePayload.setMax_players(6);

    when(gameRepository.save(existingGame)).thenReturn(existingGame);

    Game updatedGame = gameService.updateGame(updatePayload, existingGame);

    verify(gameRepository, times(1)).save(existingGame);

    assertAll(
        () -> assertEquals(updatePayload.getName(), updatedGame.getName(), "Name should be updated"),
        () -> assertEquals(updatePayload.getMax_players(), updatedGame.getMax_players(),
            "Max players should be updated"));
  }

  @Transactional
  @Test
  void testUpdateGameNotFound() {
    GameCreateDto gameCreateDto = new GameCreateDto();
    gameCreateDto.setName("NombreActualizado");
    gameCreateDto.setMax_players(7);
  }

  @Test
  void testRemovePlayerFromGame() {
    Player playerToRemove = new Player();
    Game game = new Game();

    GamePlayer playerToRemoveGamePlayer = new GamePlayer();
    playerToRemoveGamePlayer.setPlayer(playerToRemove);

    game.setCreator(playerToRemoveGamePlayer);

    doNothing().when(gameRepository).delete(game);

    gameService.removePlayerFromGame(game, playerToRemove);

    verify(gameRepository, times(1)).delete(game);

    assertTrue(game.getGame_players().isEmpty(), "Game players list should be empty");
  }

  @Test
  void testRemovePlayerFromGameFinishedGame() {
    Player playerToRemove = new Player();
    Game game = new Game();
    game.setFinish(LocalDateTime.now());

    gameService.removePlayerFromGame(game, playerToRemove);

    verify(gameRepository, never()).delete(game);

    assertTrue(game.getGame_players().isEmpty(), "Game players list should be empty");
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

    // Optional<Game> resultGameOptional =
    // gameService.addPlayerToGame(testGame.getId(), testPlayer);

    // assertTrue(resultGameOptional.isPresent());
    // Game resultGame = resultGameOptional.get();
    // assertEquals(testGame.getId(), resultGame.getId());
    // assertTrue(resultGame.getRaw_game_players().size() > 0);

    // boolean playerFoundInGame = resultGame.getRaw_game_players().stream()
    // .anyMatch(gp ->
    // gp.getPlayer().getUsername().equals(testPlayer.getUsername()));
    // assertTrue(playerFoundInGame);
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

    // gameService.addPlayerToGame(testGame.getId(), testPlayer);

    // assertThrows(RuntimeException.class, () -> {
    // gameService.addPlayerToGame(testGame.getId(), testPlayer);
    // });
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

    // Optional<Game> resultGameOptional =
    // gameService.addPlayerToGame(testGame.getId(), testPlayer);

    // assertFalse(resultGameOptional.isPresent());
  }
}
