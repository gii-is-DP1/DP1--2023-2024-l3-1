package org.springframework.samples.petclinic.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.dto.GameCreateDto;
import org.springframework.samples.petclinic.dto.PlayRequestDto;
import org.springframework.samples.petclinic.model.Game;
import org.springframework.samples.petclinic.model.GamePlayer;
import org.springframework.samples.petclinic.model.Player;
import org.springframework.samples.petclinic.services.GamePlayerService;
import org.springframework.samples.petclinic.services.GameService;
import org.springframework.samples.petclinic.services.PlayerService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RestControllerAdvice
@RequestMapping("/api/games")
@Tag(name = "Games", description = "The Games management API")
@SecurityRequirement(name = "bearerAuth")
public class GameController {

    private final GameService gameService;
    private final PlayerService playerService;
    private final GamePlayerService gamePlayerService;

    @Autowired
    public GameController(GameService gameService, PlayerService playerService, GamePlayerService gamePlayerService) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.gamePlayerService = gamePlayerService;
    }

    @Operation(summary = "Obtiene los detalles de una partida por su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada correctamente.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class)) }),
            @ApiResponse(responseCode = "404", description = "No se encuentra la partida solicitada.", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> findGame(@PathVariable("id") String id) {
        Optional<Game> gameToGet = gameService.findGame(id);
        if (!gameToGet.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Game>(gameToGet.get(), HttpStatus.OK);
    }

    @GetMapping("/me")
    @Operation(summary = "Obtiene la partida del jugador actual registrado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada correctamente.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class)) }),
            @ApiResponse(responseCode = "404", description = "El jugador actual no está en ninguna partida.", content = @Content),
            @ApiResponse(responseCode = "401", description = "El jugador actual no está autenticado.", content = @Content)
    })
    public ResponseEntity<Game> getMyGame() {
        Optional<Player> currentOptPlayer = this.playerService.findCurrentPlayer();

        if (!currentOptPlayer.isPresent()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<Game> op_currentGame = this.gameService.getCurrentGameOfPlayer(currentOptPlayer.get());

        if (op_currentGame.isPresent()) {
            return new ResponseEntity<>(op_currentGame.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Crea una nueva partida si el jugador actual está autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Partida creada correctamente.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class)) }),
            @ApiResponse(responseCode = "401", description = "El jugador actual no está autenticado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error desconocido del servidor.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Error en el cuerpo de la solicitud", content = @Content)
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Game> createGame(@Valid @RequestBody GameCreateDto gameCreateDTO) {
        Optional<Player> currentPlayer = playerService.findCurrentPlayer();

        if (currentPlayer.isPresent()) {
            return new ResponseEntity<>(this.gameService.createGame(gameCreateDTO, currentPlayer.get()), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // Cambiar el número máximo de usuarios de una partida en el lobby
    @Operation(summary = "Actualiza una partida que está en el lobby si el jugador actual es el creador y la partida está en el lobby.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada correctamente.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class)) }),
            @ApiResponse(responseCode = "401", description = "El usuario actual no está autorizado para realizar esta operación.", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encuentra la partida a actualizar.", content = @Content),
            @ApiResponse(responseCode = "423", description = "La partida no está en el estado correcto para ser actualizada (está en curso o finalizada).", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error desconocido del servidor.", content = @Content)
    })
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Game> updateGameLobby(@Valid @RequestBody GameCreateDto gameCreateDTO,
            @PathVariable("id") String id) {
        Optional<Player> op_currentPlayer = playerService.findCurrentPlayer();
        Optional<Game> op_game = gameService.findGame(id);

        if (!op_currentPlayer.isPresent()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (!op_game.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Game currentGame = op_game.get();
        Player user = op_currentPlayer.get();
        Player creator = currentGame.getCreator().getPlayer();

        if (user.getId() != creator.getId()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (currentGame.isOnLobby()) {
            Game newGame = gameService.updateGame(gameCreateDTO, currentGame);
            return new ResponseEntity<Game>(newGame, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
    }

    @Operation(summary = "Elimina al jugador actual de la partida.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada correctamente."),
            @ApiResponse(responseCode = "401", description = "El jugador actual no está autenticado."),
            @ApiResponse(responseCode = "404", description = "El jugador actual no tiene ninguna partida en curso."),
            @ApiResponse(responseCode = "500", description = "Error desconocido del servidor.")
    })
    @DeleteMapping("/me")
    public ResponseEntity<?> leaveGame() {
        Optional<Player> currentOptPlayer = playerService.findCurrentPlayer();

        if (!currentOptPlayer.isPresent()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Player currentPlayer = currentOptPlayer.get();
        Optional<Game> currentGame = this.gameService.getCurrentGameOfPlayer(currentPlayer);
        if (!currentGame.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        this.gameService.removePlayerFromGame(currentGame.get(), currentPlayer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Expulsa a un jugador de la partida (solo permitido para el creador de la partida).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada correctamente. El jugador fue expulsado de la partida."),
            @ApiResponse(responseCode = "401", description = "El jugador actual no es el creador de la partida o no está autenticado."),
            @ApiResponse(responseCode = "404", description = "El jugador a expulsar no se encontró en la partida o la partida no existe."),
            @ApiResponse(responseCode = "400", description = "No se puede expulsar a sí mismo de la partida."),
            @ApiResponse(responseCode = "423", description = "La partida no está en el estado correcto para expulsar jugadores (en curso o finalizada)."),
            @ApiResponse(responseCode = "500", description = "Error desconocido del servidor.")
    })
    @DeleteMapping("/{player_username}")
    public ResponseEntity<?> expelPlayer(@PathVariable String player_username) {
        Optional<Player> currentOptPlayer = playerService.findCurrentPlayer();

        if (!currentOptPlayer.isPresent()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Player currentPlayer = currentOptPlayer.get();
        Optional<Game> op_game = this.gameService.getCurrentGameOfPlayer(currentPlayer);

        if (!op_game.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Game game = op_game.get();

        if (currentPlayer.getId() != game.getCreator().getPlayerId()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (!game.isOnLobby()) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }

        Optional<Game> op_game_of_player_to_remove = this.gameService.getCurrentGameByUsername(player_username);
        Optional<Player> op_player_to_expel = this.playerService.findByUsernamePlayer(player_username);

        if (!op_game_of_player_to_remove.isPresent() || !op_player_to_expel.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        gameService.removePlayerFromGame(game, op_player_to_expel.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Permite a un jugador unirse a una partida existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada correctamente.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class)) }),
            @ApiResponse(responseCode = "401", description = "El jugador actual no está autenticado."),
            @ApiResponse(responseCode = "404", description = "No se encuentra la partida o jugador."),
            @ApiResponse(responseCode = "423", description = "La partida no está en el estado correcto para unirse (está en curso o finalizada)."),
            @ApiResponse(responseCode = "509", description = "La partida está completa y no se puede unir más jugadores."),
            @ApiResponse(responseCode = "304", description = "El jugador ya se encuentra en la partida"),
            @ApiResponse(responseCode = "500", description = "Error desconocido del servidor.")
    })
    @PostMapping("/join/{game_id}")
    public ResponseEntity<Game> joinGame(@PathVariable String game_id) {
        Optional<Game> optionalGameToJoin = gameService.findGame(game_id);
        Optional<Player> op_currentPlayer = playerService.findCurrentPlayer();

        if (!op_currentPlayer.isPresent()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Player currentPlayer = op_currentPlayer.get();

        if (!optionalGameToJoin.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Game gameToJoin = optionalGameToJoin.get();

        if (!gameToJoin.isOnLobby()) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }

        if (gameToJoin.isFull()) {
            return new ResponseEntity<>(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
        }

        List<Integer> player_ids_in_game = gameToJoin.getAllPlayers().stream().map(p -> p.getId()).toList();

        if (player_ids_in_game.contains(currentPlayer.getId())) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }

        this.gameService.addPlayerToGame(gameToJoin, currentPlayer);
        return new ResponseEntity<>(gameToJoin, HttpStatus.OK);
    }

    @Operation(summary = "Inicia la partida en la que se encuentre el jugador actual si se cumplen las condiciones requeridas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada correctamente.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class)) }),
            @ApiResponse(responseCode = "401", description = "El jugador actual no es el creador de la partida o no está autenticado."),
            @ApiResponse(responseCode = "404", description = "No se encuentra la partida."),
            @ApiResponse(responseCode = "423", description = "La partida no está en el estado correcto para iniciar (está en curso o finalizada)."),
            @ApiResponse(responseCode = "428", description = "No hay suficientes jugadores para iniciar la partida."),
            @ApiResponse(responseCode = "500", description = "Error desconocido del servidor.")
    })
    @PostMapping("/start")
    public ResponseEntity<Game> startGame() {
        Optional<Player> op_currentPlayer = this.playerService.findCurrentPlayer();

        if (!op_currentPlayer.isPresent()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Player currentPlayer = op_currentPlayer.get();
        Optional<Game> op_currentGame = this.gameService.getCurrentGameOfPlayer(currentPlayer);

        if (!op_currentGame.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Game currentGame = op_currentGame.get();

        if (!currentGame.isOnLobby()) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }

        Integer creator_id = currentGame.getCreator().getPlayerId();

        if (currentPlayer.getId() != creator_id) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (!currentGame.startable()) {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_REQUIRED);
        }

        return new ResponseEntity<Game>(this.gameService.startGame(currentGame), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class, type = "array")) }),
            @ApiResponse(responseCode = "204", description = "No hay partidas que mostrar", content = @Content),
            @ApiResponse(responseCode = "401", description = "El usuario actual no es administrador o no está autenticado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error desconocido del servidor", content = @Content) })
    @Operation(summary = "Lista todas las partidas. El usuario de este método debe ser administrador")
    @GetMapping
    public ResponseEntity<List<Game>> getAll() {
        Optional<Player> target = playerService.findCurrentPlayer();

        if (!target.isPresent() || !target.get().getIs_admin()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<List<Game>> gameList = gameService.findAll();

        if (!gameList.isPresent() || (gameList.isPresent() && gameList.get().isEmpty())) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<Game>>(gameList.get(), HttpStatus.OK);
    }

    @Operation(summary = "Juega una figura en la partida actual.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada correctamente."),
            @ApiResponse(responseCode = "401", description = "El jugador actual no está autenticado."),
            @ApiResponse(responseCode = "404", description = "El jugador actual no está en ninguna partida"),
            @ApiResponse(responseCode = "417", description = "El icono seleccionado no es el correcto"),
            @ApiResponse(responseCode = "500", description = "Error desconocido del servidor.")
    })
    @PostMapping("/me/play")
    public ResponseEntity<Void> play(@RequestBody PlayRequestDto playRequestDto) {
        Optional<Player> currentOptPlayer = playerService.findCurrentPlayer();

        if (!currentOptPlayer.isPresent()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Player currentPlayer = currentOptPlayer.get();
        Optional<Game> op_current_player_game = this.gameService.getCurrentGameOfPlayer(currentPlayer);

        if (!op_current_player_game.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Game current_player_game = op_current_player_game.get();
        Optional<GamePlayer> op_current_game_player = this.gamePlayerService.getGamePlayerByUsernameAndGame(currentPlayer.getUsername(), current_player_game);

        if (!op_current_game_player.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        GamePlayer current_game_player = op_current_game_player.get();

        try {
            this.gameService.playFigure(current_game_player, playRequestDto.getIcon());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException nf) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }
}
