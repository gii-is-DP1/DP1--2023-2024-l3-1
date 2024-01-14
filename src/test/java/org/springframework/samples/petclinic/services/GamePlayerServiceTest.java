package org.springframework.samples.petclinic.services;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Game;
import org.springframework.samples.petclinic.model.GamePlayer;
import org.springframework.samples.petclinic.model.Player;
import org.springframework.samples.petclinic.repositories.GamePlayerRepository;

@SpringBootTest
@AutoConfigureTestDatabase
public class GamePlayerServiceTest {

  @Mock
  private GamePlayerRepository gamePlayerRepository;

  @InjectMocks
  private GamePlayerService gamePlayerService;

  @Test
  void testSave() {
    GamePlayer gamePlayer = new GamePlayer();
    when(gamePlayerRepository.save(gamePlayer)).thenReturn(gamePlayer);

    GamePlayer savedGamePlayer = gamePlayerService.save(gamePlayer);

    verify(gamePlayerRepository).save(gamePlayer);
    assertEquals(gamePlayer, savedGamePlayer);
  }

  @Test
  void testCreateGamePlayerCreator() {
    Player player = new Player();

    GamePlayer gamePlayer = gamePlayerService.createGamePlayerCreator(player);

    assertNotNull(gamePlayer);
    assertNull(gamePlayer.getGame());
    assertEquals(player, gamePlayer.getPlayer());
  }

  @Test
  void testCreateGamePlayer() {
    Player player = new Player();
    Game game = new Game();

    when(gamePlayerRepository.save(any(GamePlayer.class))).thenAnswer(invocation -> {
      GamePlayer savedGamePlayer = invocation.getArgument(0);
      savedGamePlayer.setId(1);
      return savedGamePlayer;
    });

    GamePlayer gamePlayer = gamePlayerService.createGamePlayer(player, game);

    assertNotNull(gamePlayer);
    assertEquals(player, gamePlayer.getPlayer());
    assertEquals(game, gamePlayer.getGame());
    assertNotNull(gamePlayer.getId());
  }

  @Test
  void testGetGamePlayerByUsernameAndGame() {
    String username = "testUser";
    Game game = new Game();
    when(gamePlayerRepository.findGamePlayerByUsernameAndGame(username, game.getId()))
        .thenReturn(Optional.of(new GamePlayer()));

    Optional<GamePlayer> result = gamePlayerService.getGamePlayerByUsernameAndGame(username, game);

    assertTrue(result.isPresent());
  }

}
