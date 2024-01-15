package com.dobble.controllers;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dobble.controllers.GameController;
import com.dobble.dto.GameCreateDto;
import com.dobble.dto.PlayRequestDto;
import com.dobble.model.Game;
import com.dobble.model.GamePlayer;
import com.dobble.model.Player;
import com.dobble.model.enums.Icon;
import com.dobble.services.CardService;
import com.dobble.services.GamePlayerService;
import com.dobble.services.GameService;
import com.dobble.services.PlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import jakarta.security.auth.message.AuthException;

@ExtendWith(MockitoExtension.class)
public class GameControllerTest {

  @Mock
  private GameService gameService;

  @Mock
  private PlayerService playerService;

  @Mock
  private CardService cardService;

  @Mock
  private GamePlayerService gamePlayerService;

  @InjectMocks
  private GameController gameController;

  @Test
  public void testFindGame() {
    String gameId = "1";
    Game mockGame = new Game();
    when(gameService.findGame(gameId)).thenReturn(Optional.of(mockGame));

    ResponseEntity<?> response = gameController.findGame(gameId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockGame, response.getBody());
  }

  @Test
  public void testFindGameNotFound() {
    String gameId = "1";
    when(gameService.findGame(gameId)).thenReturn(Optional.empty());

    ResponseEntity<?> response = gameController.findGame(gameId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  public void testGetMyGame() {
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(new Player()));
    when(gameService.getCurrentGameOfPlayer(any())).thenReturn(Optional.of(new Game()));

    ResponseEntity<Game> responseEntity = gameController.getMyGame();

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
  }

  @Test
  public void testGetMyGamePlayerNotInGame() {
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(new Player()));
    when(gameService.getCurrentGameOfPlayer(any())).thenReturn(Optional.empty());

    ResponseEntity<Game> responseEntity = gameController.getMyGame();

    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    assertNull(responseEntity.getBody());
  }

  @Test
  public void testGetMyGameAuthenticatedWithoutGame() {
    Player mockPlayer = new Player();
    mockPlayer.setId(1);
    // mockPlayer.setCurrentGame(null);

    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(mockPlayer));

    ResponseEntity<Game> response = gameController.getMyGame();

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testGetMyGameUnauthenticated() {
    when(playerService.findCurrentPlayer()).thenReturn(Optional.empty());

    ResponseEntity<Game> response = gameController.getMyGame();

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }

  @Test
  public void testCreateGame() {
    GameCreateDto gameCreateDto = new GameCreateDto();
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(new Player()));
    when(gameService.createGame(any(), any())).thenReturn(new Game());

    ResponseEntity<Game> responseEntity = gameController.createGame(gameCreateDto);

    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
  }

  @Test
  public void testCreateGameUnauthorized() throws AuthException {
    when(playerService.findCurrentPlayer()).thenReturn(Optional.empty());

    ResponseEntity<Game> response = gameController.createGame(new GameCreateDto());

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  @Transactional
  public void testUpdateGameLobby() {
    GameCreateDto gameCreateDto = new GameCreateDto();
    String gameId = "123";
    Game game = new Game();
    game.setId(gameId);
    Player player = new Player();
    GamePlayer gamePlayer = new GamePlayer();
    gamePlayer.setPlayer(player);
    game.setCreator(gamePlayer);
    gameService.saveGame(game);

    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(player));
    when(gameService.findGame(gameId)).thenReturn(Optional.of(game));
    when(gameService.updateGame(any(), any())).thenReturn(new Game());

    ResponseEntity<Game> responseEntity = gameController.updateGameLobby(gameCreateDto, gameId);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
  }

  @Test
  public void testUpdateGameLobbyUnauthorized() {
    GameCreateDto gameCreateDto = new GameCreateDto();
    String gameId = "123";
    when(playerService.findCurrentPlayer()).thenReturn(Optional.empty());

    ResponseEntity<Game> responseEntity = gameController.updateGameLobby(gameCreateDto, gameId);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    assertNull(responseEntity.getBody());
  }

  @Test
  public void testUpdateGameLobbyNotFound() {
    GameCreateDto gameCreateDto = new GameCreateDto();
    String gameId = "123";
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(new Player()));
    when(gameService.findGame(gameId)).thenReturn(Optional.empty());

    ResponseEntity<Game> responseEntity = gameController.updateGameLobby(gameCreateDto, gameId);

    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    assertNull(responseEntity.getBody());
  }

  @Test
  public void testUpdateGameLobbyUnauthorizedUser() {
    GameCreateDto gameCreateDto = new GameCreateDto();
    String gameId = "123";
    Game mockGame = new Game();

    Player player = new Player();
    GamePlayer gamePlayer = new GamePlayer();
    gamePlayer.setPlayer(player);
    mockGame.setCreator(gamePlayer);

    when(playerService.findCurrentPlayer()).thenReturn(Optional.empty());
    when(gameService.findGame(gameId)).thenReturn(Optional.of(mockGame));

    ResponseEntity<Game> responseEntity = gameController.updateGameLobby(gameCreateDto, gameId);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  @Test
  public void testUpdateGameLobbyGameNotInLobby() {
    GameCreateDto gameCreateDto = new GameCreateDto();
    String gameId = "123";
    Game ongoingGame = new Game();
    ongoingGame.setStart(LocalDateTime.now());
    GamePlayer gamePlayer = new GamePlayer();
    Player mockCurrentPlayer = new Player();
    gamePlayer.setPlayer(mockCurrentPlayer);
    ongoingGame.setCreator(gamePlayer);

    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(mockCurrentPlayer));

    when(gameService.findGame(gameId)).thenReturn(Optional.of(ongoingGame));

    ResponseEntity<Game> responseEntity = gameController.updateGameLobby(gameCreateDto, gameId);

    assertEquals(HttpStatus.LOCKED, responseEntity.getStatusCode());
    assertNull(responseEntity.getBody());
  }

  @Test
  public void testLeaveGame() {
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(new Player()));
    when(gameService.getCurrentGameOfPlayer(any())).thenReturn(Optional.of(new Game()));

    ResponseEntity<?> responseEntity = gameController.leaveGame();

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }

  @Test
  public void testLeaveGameUnauthorized() {
    when(playerService.findCurrentPlayer()).thenReturn(Optional.empty());

    ResponseEntity<?> responseEntity = gameController.leaveGame();

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  @Test
  public void testLeaveGameNoCurrentGame() {
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(new Player()));
    when(gameService.getCurrentGameOfPlayer(any())).thenReturn(Optional.empty());

    ResponseEntity<?> responseEntity = gameController.leaveGame();

    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
  }

  @Test
  public void testExpelPlayer() {
    Player mockCurrentPlayer = new Player();
    Game mockGame = new Game();
    Player mockPlayerToExpel = new Player();
    GamePlayer gamePlayer = new GamePlayer();
    gamePlayer.setPlayer(mockCurrentPlayer);
    mockGame.setCreator(gamePlayer);

    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(mockCurrentPlayer));
    when(gameService.getCurrentGameOfPlayer(mockCurrentPlayer)).thenReturn(Optional.of(mockGame));
    when(gameService.getCurrentGameByUsername(anyString())).thenReturn(Optional.of(mockGame));
    when(playerService.findByUsernamePlayer(anyString())).thenReturn(Optional.of(mockPlayerToExpel));

    ResponseEntity<?> responseEntity = gameController.expelPlayer("player_to_expel");

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    verify(gameService).removePlayerFromGame(mockGame, mockPlayerToExpel);
  }

  @Test
  public void testExpelPlayerUnauthorized() {
    when(playerService.findCurrentPlayer()).thenReturn(Optional.empty());

    ResponseEntity<?> responseEntity = gameController.expelPlayer("player_to_expel");

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  @Test
  public void testExpelPlayerNotFound() {
    Player mockCurrentPlayer = new Player();
    mockCurrentPlayer.setId(1);
    Game mockGame = new Game();
    GamePlayer gamePlayer = new GamePlayer();
    gamePlayer.setPlayer(mockCurrentPlayer);
    mockGame.setCreator(gamePlayer);

    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(mockCurrentPlayer));
    when(gameService.getCurrentGameOfPlayer(mockCurrentPlayer)).thenReturn(Optional.of(mockGame));
    when(gameService.getCurrentGameByUsername(anyString())).thenReturn(Optional.empty());

    ResponseEntity<?> responseEntity = gameController.expelPlayer("player_to_expel");

    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    verify(gameService, never()).removePlayerFromGame(any(), any());
  }

  @Test
  public void testJoinGame() {
    Player mockCurrentPlayer = new Player();
    Game mockGameToJoin = new Game();
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(mockCurrentPlayer));
    when(gameService.findGame(anyString())).thenReturn(Optional.of(mockGameToJoin));

    ResponseEntity<Game> responseEntity = gameController.joinGame("game_id");

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    verify(gameService).addPlayerToGame(mockGameToJoin, mockCurrentPlayer);
  }

  @Test
  public void testJoinGameUnauthorized() {
    when(playerService.findCurrentPlayer()).thenReturn(Optional.empty());

    ResponseEntity<Game> responseEntity = gameController.joinGame("game_id");

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    verify(gameService, never()).addPlayerToGame(any(), any());
  }

  @Test
  public void testJoinGameNotFound() {
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(new Player()));
    when(gameService.findGame(anyString())).thenReturn(Optional.empty());

    ResponseEntity<Game> responseEntity = gameController.joinGame("game_id");

    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    verify(gameService, never()).addPlayerToGame(any(), any());
  }

  @Test
  public void testJoinGameLocked() {
    Player mockCurrentPlayer = new Player();
    Game mockGameToJoin = new Game();
    mockGameToJoin.setStart(LocalDateTime.now());
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(mockCurrentPlayer));
    when(gameService.findGame(anyString())).thenReturn(Optional.of(mockGameToJoin));

    ResponseEntity<Game> responseEntity = gameController.joinGame("game_id");

    assertEquals(HttpStatus.LOCKED, responseEntity.getStatusCode());
    verify(gameService, never()).addPlayerToGame(any(), any());
  }

  @Test
  public void testJoinGameBandwidthLimitExceeded() {
    Player mockCurrentPlayer = new Player();
    Game mockGameToJoin = new Game();
    mockGameToJoin.setMax_players(1);
    GamePlayer gamePlayer = new GamePlayer();
    gamePlayer.setPlayer(mockCurrentPlayer);
    mockGameToJoin.setGame_players(List.of(gamePlayer));
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(mockCurrentPlayer));
    when(gameService.findGame(anyString())).thenReturn(Optional.of(mockGameToJoin));

    ResponseEntity<Game> responseEntity = gameController.joinGame("game_id");

    assertEquals(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, responseEntity.getStatusCode());
    verify(gameService, never()).addPlayerToGame(any(), any());
  }

  @Test
  public void testStartGame() {
    Player mockCurrentPlayer = new Player();
    Game mockCurrentGame = new Game();
    GamePlayer gamePlayer = new GamePlayer();
    gamePlayer.setPlayer(mockCurrentPlayer);
    mockCurrentGame.setCreator(gamePlayer);
    List<GamePlayer> players = new ArrayList<GamePlayer>();
    players.add(new GamePlayer());
    players.add(gamePlayer);
    mockCurrentGame.setGame_players(players);
    mockCurrentGame.setMax_players(2);

    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(mockCurrentPlayer));
    when(gameService.getCurrentGameOfPlayer(mockCurrentPlayer)).thenReturn(Optional.of(mockCurrentGame));
    when(gameService.startGame(mockCurrentGame)).thenReturn(mockCurrentGame);

    ResponseEntity<Game> responseEntity = gameController.startGame();

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    verify(gameService).startGame(mockCurrentGame);
  }

  @Test
  public void testStartGameUnauthorized() {
    when(playerService.findCurrentPlayer()).thenReturn(Optional.empty());

    ResponseEntity<Game> responseEntity = gameController.startGame();

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    verify(gameService, never()).startGame(any());
  }

  @Test
  public void testStartGameNotFound() {
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(new Player()));
    when(gameService.getCurrentGameOfPlayer(any())).thenReturn(Optional.empty());

    ResponseEntity<Game> responseEntity = gameController.startGame();

    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    verify(gameService, never()).startGame(any());
  }

  @Test
  public void testStartGameLocked() {
    Player mockCurrentPlayer = new Player();
    Game mockCurrentGame = new Game();
    mockCurrentGame.setStart(LocalDateTime.now());
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(mockCurrentPlayer));
    when(gameService.getCurrentGameOfPlayer(mockCurrentPlayer)).thenReturn(Optional.of(mockCurrentGame));

    ResponseEntity<Game> responseEntity = gameController.startGame();

    assertEquals(HttpStatus.LOCKED, responseEntity.getStatusCode());
    verify(gameService, never()).startGame(any());
  }

  @Test
  public void testStartGameUnauthorizedCreator() {
    Player mockPlayer = new Player();
    Game mockCurrentGame = new Game();
    GamePlayer gamePlayer = new GamePlayer();
    gamePlayer.setPlayer(mockPlayer);
    mockCurrentGame.setCreator(gamePlayer);
    List<GamePlayer> players = new ArrayList<GamePlayer>();
    players.add(new GamePlayer());
    players.add(gamePlayer);
    mockCurrentGame.setGame_players(players);
    mockCurrentGame.setMax_players(2);

    when(playerService.findCurrentPlayer()).thenReturn(Optional.empty());

    ResponseEntity<Game> responseEntity = gameController.startGame();

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    verify(gameService, never()).startGame(any());
  }

  @Test
  public void testStartGamePreconditionRequired() {
    Player mockCurrentPlayer = new Player();
    Game mockCurrentGame = new Game();
    GamePlayer gamePlayer = new GamePlayer();
    gamePlayer.setPlayer(mockCurrentPlayer);
    mockCurrentGame.setCreator(gamePlayer);

    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(mockCurrentPlayer));
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(mockCurrentPlayer));
    when(gameService.getCurrentGameOfPlayer(mockCurrentPlayer)).thenReturn(Optional.of(mockCurrentGame));

    ResponseEntity<Game> responseEntity = gameController.startGame();

    assertEquals(HttpStatus.PRECONDITION_REQUIRED, responseEntity.getStatusCode());
    verify(gameService, never()).startGame(any());
  }

  @Test
  void testGetAll() {
    Player adminPlayer = new Player();
    adminPlayer.setId(1);
    adminPlayer.setIs_admin(true);

    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(adminPlayer));

    List<Game> gameList = new ArrayList<>();
    gameList.add(new Game());

    when(gameService.findAll()).thenReturn(Optional.of(gameList));

    ResponseEntity<List<Game>> response = gameController.getAll();

    assertEquals(HttpStatus.OK, response.getStatusCode());

    assertEquals(gameList, response.getBody());
  }

  @Test
  public void testGetAllUnauthorized() {
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(new Player()));

    ResponseEntity<List<Game>> response = gameController.getAll();

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  public void testPlay() {
    Player mockCurrentPlayer = new Player();
    Game mockCurrentGame = new Game();
    GamePlayer gamePlayer = new GamePlayer();
    gamePlayer.setPlayer(mockCurrentPlayer);
    mockCurrentGame.setCreator(gamePlayer);

    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(mockCurrentPlayer));
    when(gameService.getCurrentGameOfPlayer(mockCurrentPlayer)).thenReturn(Optional.of(mockCurrentGame));
    when(gamePlayerService.getGamePlayerByUsernameAndGame(mockCurrentPlayer.getUsername(), mockCurrentGame))
        .thenReturn(Optional.of(gamePlayer));

    PlayRequestDto playRequestDto = new PlayRequestDto();
    playRequestDto.setIcon(Icon.AGUA);

    ResponseEntity<Void> responseEntity = gameController.play(playRequestDto);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }

  @Test
  public void testPlayUnauthorized() {
    when(playerService.findCurrentPlayer()).thenReturn(Optional.empty());

    PlayRequestDto playRequestDto = new PlayRequestDto();
    playRequestDto.setIcon(Icon.AGUA);

    ResponseEntity<Void> responseEntity = gameController.play(playRequestDto);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  @Test
  public void testPlayGameNotFound() {
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(new Player()));
    when(gameService.getCurrentGameOfPlayer(any())).thenReturn(Optional.empty());

    PlayRequestDto playRequestDto = new PlayRequestDto();
    playRequestDto.setIcon(Icon.AGUA);

    ResponseEntity<Void> responseEntity = gameController.play(playRequestDto);

    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
  }

  @Test
  public void testPlayGamePlayerNotFound() {
    Player mockCurrentPlayer = new Player();
    Game mockCurrentGame = new Game();
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(mockCurrentPlayer));
    when(gameService.getCurrentGameOfPlayer(mockCurrentPlayer)).thenReturn(Optional.of(mockCurrentGame));
    when(gamePlayerService.getGamePlayerByUsernameAndGame(any(), any())).thenReturn(Optional.empty());

    PlayRequestDto playRequestDto = new PlayRequestDto();
    playRequestDto.setIcon(Icon.AGUA);

    ResponseEntity<Void> responseEntity = gameController.play(playRequestDto);

    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
  }

}
