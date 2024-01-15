package com.dobble.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.dobble.model.Game;
import com.dobble.model.GamePlayer;
import com.dobble.model.Player;

@SpringBootTest
@AutoConfigureTestDatabase
@ExtendWith(MockitoExtension.class)
public class GamePlayerRepositoryTest {

  @MockBean
  private GamePlayerRepository gamePlayerRepository;

  @Test
  void testFindGamePlayerByUsername() {
    String username = "testUser";

    Player player = new Player();
    player.setUsername(username);

    GamePlayer gamePlayer1 = new GamePlayer();
    gamePlayer1.setPlayer(player);

    GamePlayer gamePlayer2 = new GamePlayer();
    gamePlayer2.setPlayer(player);

    List<GamePlayer> expectedGamePlayers = Arrays.asList(gamePlayer1, gamePlayer2);

    when(gamePlayerRepository.findGamePlayerByUsername(username))
        .thenReturn(Optional.of(expectedGamePlayers));

    Optional<List<GamePlayer>> actualGamePlayers = gamePlayerRepository.findGamePlayerByUsername(username);

    assertEquals(expectedGamePlayers, actualGamePlayers.orElse(null));
    verify(gamePlayerRepository, times(1)).findGamePlayerByUsername(username);
  }

  @Test
  void testFindGamePlayerByUsernameAndGame() {
    String username = "testUser";
    String gameId = "testGameId";

    Player player = new Player();
    player.setUsername(username);

    Game game = new Game();
    game.setId(gameId);

    GamePlayer expectedGamePlayer = new GamePlayer();
    expectedGamePlayer.setPlayer(player);
    expectedGamePlayer.setGame(game);

    when(gamePlayerRepository.findGamePlayerByUsernameAndGame(username, gameId))
        .thenReturn(Optional.of(expectedGamePlayer));

    Optional<GamePlayer> actualGamePlayer = gamePlayerRepository.findGamePlayerByUsernameAndGame(username, gameId);

    assertEquals(expectedGamePlayer, actualGamePlayer.orElse(null));
    verify(gamePlayerRepository, times(1)).findGamePlayerByUsernameAndGame(username, gameId);
  }

}
