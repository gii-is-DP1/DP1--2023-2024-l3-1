package org.springframework.samples.petclinic.services;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.controllers.GameController;
import org.springframework.samples.petclinic.dto.GameCreateDto;
import org.springframework.samples.petclinic.model.Game;
import org.springframework.samples.petclinic.model.Player;
import org.springframework.security.test.context.support.WithMockUser;

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
  private HandService handService;

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
  public void testGetMyGameAuthenticated() {
    Player mockPlayer = new Player();
    mockPlayer.setId(1);
    Game mockGame = new Game();
    mockGame.setId("gameId");
    mockPlayer.setCurrentGame(mockGame);

    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(mockPlayer));

    ResponseEntity<Game> response = gameController.getMyGame();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockGame, response.getBody());
  }

  @Test
  public void testGetMyGameAuthenticatedWithoutGame() {
    Player mockPlayer = new Player();
    mockPlayer.setId(1);
    mockPlayer.setCurrentGame(null);

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
  public void testCreateGame() throws AuthException {
    GameCreateDto gameCreateDto = new GameCreateDto();
    gameCreateDto.setName("TestGame");
    gameCreateDto.setMax_players(4);

    Game mockGame = new Game();
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(new Player()));
    when(gameService.saveGame(any())).thenReturn(mockGame);
    when(gameService.addPlayerToGame(any(), any())).thenReturn(Optional.of(mockGame));

    ResponseEntity<Game> response = gameController.createGame(gameCreateDto);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(mockGame, response.getBody());
  }

  @Test
  public void testCreateGameUnauthorized() throws AuthException {
    when(playerService.findCurrentPlayer()).thenReturn(Optional.empty());

    ResponseEntity<Game> response = gameController.createGame(new GameCreateDto());

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  @WithMockUser(username = "dobble", password = "dobble")
  public void testUpdateGameLobby() {
    String gameId = "1";
    GameCreateDto gameCreateDto = new GameCreateDto();
    gameCreateDto.setName("UpdatedGame");
    gameCreateDto.setMax_players(6);

    Game mockGame = new Game();
    mockGame.setId(gameId);
    mockGame.setName("OriginalGame");
    mockGame.setMax_players(4);

    Player mockPlayer = new Player();
    mockPlayer.setId(1);
    mockGame.setRaw_creator(mockPlayer);

    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(mockPlayer));
    when(gameService.findGame(gameId)).thenReturn(Optional.of(mockGame));
    when(gameService.updateGame(any(), any())).thenReturn(Optional.of(mockGame));

    ResponseEntity<Game> response = gameController.updateGameLobby(gameCreateDto, gameId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockGame, response.getBody());
  }

  @Test
  public void testJoinGame() throws AuthException {
    GameService gameService = mock(GameService.class);
    PlayerService playerService = mock(PlayerService.class);

    GameController controller = new GameController(gameService, playerService, cardService, handService,
        gamePlayerService);

    Player mockPlayer = new Player();
    mockPlayer.setId(1);
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(mockPlayer));

    Game mockGame = new Game();
    mockGame.setId("gameId");
    when(gameService.findGame("gameId")).thenReturn(Optional.of(mockGame));
    when(gameService.addPlayerToGame("gameId", mockPlayer)).thenReturn(Optional.of(mockGame));
    when(gameService.saveGame(mockGame)).thenReturn(mockGame);

    ResponseEntity<Game> response = controller.joinGame("gameId");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockGame, response.getBody());
  }

  @Test
  void testStartGame() {
    Player creator = new Player();
    creator.setId(1);

    Player currentPlayer = new Player();
    currentPlayer.setId(1);
    currentPlayer.setIs_admin(true);

    Game gameInLobby = new Game();
    gameInLobby.setId("gameId");
    gameInLobby.setRaw_creator(currentPlayer);
    List<Player> players = new ArrayList<>();
    Player player2 = new Player();
    players.add(currentPlayer);
    players.add(player2);
    gameInLobby.setRaw_players(players);
    gameInLobby.setStart(null);

    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(currentPlayer));
    when(gameService.findGame("gameId")).thenReturn(Optional.of(gameInLobby));

    ResponseEntity<Game> response = gameController.startGame("gameId");

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testStartGameUnauthorized() {
    String gameId = "1";
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(new Player()));
    when(gameService.findGame(gameId)).thenReturn(Optional.of(new Game()));

    ResponseEntity<Game> response = gameController.startGame(gameId);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertNull(response.getBody());
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
    Player mockPlayer = new Player();
    mockPlayer.setId(1);
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(mockPlayer));
    doNothing().when(gameService).playFigure(anyString(), anyInt(), anyInt());

    ResponseEntity<Void> response = gameController.play("gameId", 1);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(gameService, times(1)).playFigure("gameId", 1, 1); // Assuming figureId is 1 for simplicity
  }

  @Test
  public void testPlayWithException() {
    Player mockPlayer = new Player();
    mockPlayer.setId(1);
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(mockPlayer));
    doThrow(new RuntimeException("Simulated exception")).when(gameService).playFigure(anyString(), anyInt(), anyInt());

    ResponseEntity<Void> response = gameController.play("gameId", 1);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
}
