package org.springframework.samples.petclinic.controllers;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.dto.GameCreateDto;
import org.springframework.samples.petclinic.model.Game;
import org.springframework.samples.petclinic.model.Player;
import org.springframework.samples.petclinic.services.GameService;
import org.springframework.samples.petclinic.services.PlayerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/games")
@Tag(name = "Games", description = "The Games management API")
@SecurityRequirement(name = "bearerAuth")
public class GameController {

    private final GameService gameService;
    private final PlayerService playerService;

    @Autowired
    public GameController(GameService gameService, PlayerService playerService) {
        this.gameService = gameService;
        this.playerService = playerService;
    }

    @Operation(summary = "Obtiene los detalles de una partida por su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada correctamente.", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encuentra la partida solicitada.", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Game> findGame(@PathVariable("id") String id) {
        Game gameToGet = gameService.findGame(id);
        if (gameToGet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Game>(gameToGet, HttpStatus.OK);
    }

    @Operation(summary = "Crea una nueva partida si el jugador actual está autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Partida creada correctamente.", content = @Content),
            @ApiResponse(responseCode = "401", description = "El jugador actual no está autenticado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error desconocido del servidor.", content = @Content)
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Game> createGame(@Valid @RequestBody GameCreateDto gameCreateDTO) {
        try {
            Optional<Player> currentPlayer = playerService.findCurrentPlayer();
            if (currentPlayer.isPresent()) {
                Game game = new Game();
                Set<Player> ls = Set.of(currentPlayer.get());

                game.setName(gameCreateDTO.getName());
                game.setMaxPlayers(gameCreateDTO.getMaxPlayers());
                game.setCreator(currentPlayer.get());
                game.setPlayers(ls);
                gameService.saveGame(game);
                return new ResponseEntity<>(game, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Cambiar el número máximo de usuarios de una partida en el lobby
    @Operation(summary = "Actualiza una partida en el estado de lobby si el jugador actual es el creador y la partida está en el lobby.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada correctamente.", content = @Content),
            @ApiResponse(responseCode = "401", description = "El usuario actual no está autorizado para realizar esta operación.", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encuentra la partida a actualizar.", content = @Content),
            @ApiResponse(responseCode = "423", description = "La partida no está en el estado correcto para ser actualizada (está en curso o finalizada).", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error desconocido del servidor.", content = @Content)
    })
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Game> updateGameLobby(@Valid @RequestBody GameCreateDto gameCreateDTO,
            @PathVariable("id") String id) {
        Game currentGame = gameService.findGame(id);

        if (currentGame == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Player> currentPlayer = playerService.findCurrentPlayer();
        Player creator = currentGame.getCreator();

        if (!currentPlayer.isPresent() || !currentPlayer.get().equals(creator)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (currentGame.isOnLobby()) {
            Game newGame = gameService.updateGame(gameCreateDTO, id);
            return new ResponseEntity<>(newGame, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }

    }

    @Operation(summary = "Permite a un jugador unirse a una partida existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada correctamente.", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encuentra la partida o jugador.", content = @Content),
            @ApiResponse(responseCode = "423", description = "La partida no está en el estado correcto para unirse (está en curso o finalizada).", content = @Content),
            @ApiResponse(responseCode = "509", description = "El jugador ya se encuentra en la partida.", content = @Content),
            @ApiResponse(responseCode = "409", description = "La partida está completa y no se puede unir más jugadores.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error desconocido del servidor.", content = @Content)
    })
    @PostMapping("/join/{gameId}")
    public ResponseEntity<Game> joinGame(@PathVariable String gameId) {
        Game gameToJoin = gameService.findGame(gameId);
        Optional<Player> currentPlayer = playerService.findCurrentPlayer();

        if (gameToJoin == null && !currentPlayer.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!gameToJoin.isOnLobby()) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }

        if (gameToJoin.getPlayers().contains(currentPlayer.get())) {
            return new ResponseEntity<>(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
        }

        if (gameToJoin.getMaxPlayers() >= gameToJoin.getPlayers().size() + 1) {
            Player joiningPlayer = currentPlayer.get();
            gameToJoin.getPlayers().add(joiningPlayer);
            return new ResponseEntity<>(gameService.saveGame(gameToJoin), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @Operation(summary = "Inicia una partida si se cumplen las condiciones requeridas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada correctamente.", content = @Content),
            @ApiResponse(responseCode = "401", description = "El jugador actual no es el creador de la partida.", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encuentra la partida.", content = @Content),
            @ApiResponse(responseCode = "423", description = "La partida no está en el estado correcto para iniciar (está en curso o finalizada).", content = @Content),
            @ApiResponse(responseCode = "428", description = "No hay suficientes jugadores para iniciar la partida.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error desconocido del servidor.", content = @Content)
    })
    @PostMapping("/start/{gameId}")
    public ResponseEntity<Game> startGame(@PathVariable String gameId) {
        Game currentGame = gameService.findGame(gameId);
        Optional<Player> currentPlayer = playerService.findCurrentPlayer();

        if (currentGame == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!currentGame.isOnLobby()) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }

        Player creator = currentGame.getCreator();

        if (!currentPlayer.isPresent() || !currentPlayer.get().equals(creator)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (currentGame.getPlayers().size() >= 2) {
            currentGame.setStart(LocalDateTime.now());
            return new ResponseEntity<>(gameService.saveGame(currentGame), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_REQUIRED);
        }
    }
}
