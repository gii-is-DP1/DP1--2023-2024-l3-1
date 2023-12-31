package org.springframework.samples.petclinic.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.dto.GameCreateDto;
import org.springframework.samples.petclinic.model.Card;
import org.springframework.samples.petclinic.model.Game;
import org.springframework.samples.petclinic.model.GamePlayer;
import org.springframework.samples.petclinic.model.Hand;
import org.springframework.samples.petclinic.model.Player;
import org.springframework.samples.petclinic.services.CardService;
import org.springframework.samples.petclinic.services.GamePlayerService;
import org.springframework.samples.petclinic.services.GameService;
import org.springframework.samples.petclinic.services.HandService;
import org.springframework.samples.petclinic.services.PlayerService;
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
    private final CardService cardService;
    private final HandService handService;
    private final GamePlayerService gamePlayerService;

    @Autowired
    public GameController(GameService gameService, PlayerService playerService, CardService cardService,
            HandService handService, GamePlayerService gamePlayerService) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.cardService = cardService;
        this.handService = handService;
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

    @GetMapping("/myGame")
    @Operation(summary = "Obtiene la partida del jugador actual registrado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada correctamente.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class)) }),
            @ApiResponse(responseCode = "404", description = "El jugador actual no está en ninguna partida.", content = @Content),
            @ApiResponse(responseCode = "401", description = "El jugador actual no está autenticado.", content = @Content)
    })
    public ResponseEntity<Game> getMyGame() {
        Optional<Player> currentOptPlayer = playerService.findCurrentPlayer();
        try {
            if (!currentOptPlayer.isPresent()) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            Player currentPlayer = currentOptPlayer.get();
            Game currentGame = currentPlayer.getCurrentGame();

            if (currentGame != null) {
                return new ResponseEntity<>(currentGame, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(summary = "Crea una nueva partida si el jugador actual está autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Partida creada correctamente.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class)) }),
            @ApiResponse(responseCode = "401", description = "El jugador actual no está autenticado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error desconocido del servidor.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Error al añadir jugador a la partida", content = @Content)

    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Game> createGame(@Valid @RequestBody GameCreateDto gameCreateDTO) {
        try {
            Optional<Player> currentPlayer = playerService.findCurrentPlayer();
            if (currentPlayer.isPresent()) {
                Game game = new Game();
                List<Player> ls = List.of(currentPlayer.get());
                game.setName(gameCreateDTO.getName());
                game.setMax_players(gameCreateDTO.getMax_players());
                game.setRaw_creator(currentPlayer.get());
                game.setRaw_players(ls);

                game = gameService.saveGame(game);
                Optional<Game> updatedGame = gameService.addPlayerToGame(game.getId(), currentPlayer.get());

                if (updatedGame.isPresent()) {
                    return new ResponseEntity<>(updatedGame.get(), HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            } else {

                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Cambiar el número máximo de usuarios de una partida en el lobby
    @Operation(summary = "Actualiza una partida en el estado de lobby si el jugador actual es el creador y la partida está en el lobby.")
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
        Optional<Game> optionalGame = gameService.findGame(id);

        if (!optionalGame.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Game currentGame = optionalGame.get();
        Optional<Player> currentPlayer = playerService.findCurrentPlayer();
        Player creator = currentGame.getRaw_creator();

        if (!currentPlayer.isPresent() || !currentPlayer.get().equals(creator)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (currentGame.isOnLobby()) {
            Optional<Game> newGame = gameService.updateGame(gameCreateDTO, id);
            return new ResponseEntity<Game>(newGame.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }

    }

    @Operation(summary = "Permite a un jugador unirse a una partida existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada correctamente.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class)) }),
            @ApiResponse(responseCode = "404", description = "No se encuentra la partida o jugador.", content = @Content),
            @ApiResponse(responseCode = "423", description = "La partida no está en el estado correcto para unirse (está en curso o finalizada).", content = @Content),
            @ApiResponse(responseCode = "509", description = "El jugador ya se encuentra en la partida.", content = @Content),
            @ApiResponse(responseCode = "409", description = "La partida está completa y no se puede unir más jugadores.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error desconocido del servidor.", content = @Content)
    })
    @PostMapping("/join/{gameId}")
    public ResponseEntity<Game> joinGame(@PathVariable String gameId) {
        Optional<Game> optionalGameToJoin = gameService.findGame(gameId);
        Optional<Player> currentPlayer = playerService.findCurrentPlayer();

        if (!optionalGameToJoin.isPresent() && !currentPlayer.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Game gameToJoin = optionalGameToJoin.get();

        if (!gameToJoin.isOnLobby()) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }

        if (gameToJoin.getRaw_players().contains(currentPlayer.get())) {
            return new ResponseEntity<>(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
        }

        if (!gameToJoin.isFull()) {
            Player joiningPlayer = currentPlayer.get();
            gameToJoin.getRaw_players().add(joiningPlayer);
            Optional<Game> updatedGame = gameService.addPlayerToGame(gameToJoin.getId(), currentPlayer.get());

            return new ResponseEntity<Game>(gameService.saveGame(updatedGame.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @Operation(summary = "Inicia una partida si se cumplen las condiciones requeridas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada correctamente.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class)) }),
            @ApiResponse(responseCode = "401", description = "El jugador actual no es el creador de la partida.", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encuentra la partida.", content = @Content),
            @ApiResponse(responseCode = "423", description = "La partida no está en el estado correcto para iniciar (está en curso o finalizada).", content = @Content),
            @ApiResponse(responseCode = "428", description = "No hay suficientes jugadores para iniciar la partida.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error desconocido del servidor.", content = @Content)
    })
    @PostMapping("/start/{gameId}")
    public ResponseEntity<Game> startGame(@PathVariable String gameId) {
        Optional<Game> optionalCurrentGame = gameService.findGame(gameId);
        Optional<Player> currentPlayer = playerService.findCurrentPlayer();

        if (!optionalCurrentGame.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Game currentGame = optionalCurrentGame.get();

        if (!currentGame.isOnLobby()) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }

        Player creator = currentGame.getRaw_creator();

        if (!currentPlayer.isPresent() || !currentPlayer.get().equals(creator)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (currentGame.getPlayers().size() >= 2) {
            initializeGame(currentGame);
            return new ResponseEntity<Game>(gameService.saveGame(currentGame), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_REQUIRED);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class, type = "array")) }),
            @ApiResponse(responseCode = "204", description = "No hay partidas que mostrar", content = @Content),
            @ApiResponse(responseCode = "401", description = "El usuario actual no es administrador", content = @Content),
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
            @ApiResponse(responseCode = "204", description = "Operación realizada correctamente."),
            @ApiResponse(responseCode = "500", description = "Error desconocido del servidor.")
    })
    @PostMapping("/{gameId}/play")
    public ResponseEntity<Void> play(@PathVariable String gameId,@RequestBody Integer figureId){
        try {
            Optional<Player> currentOptPlayer = playerService.findCurrentPlayer(); 
            gameService.playFigure(gameId, currentOptPlayer.get().getId(), figureId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //TODO Pasar a GameService este método
    private void initializeGame(Game currentGame) {
        try {
            List<Card> allCards = cardService.findAll()
                    .orElseThrow(() -> new RuntimeException("No se encontraron cartas."));

            List<GamePlayer> gamePlayers = new ArrayList<>(currentGame.getRaw_game_players());
            Integer totalPlayers = gamePlayers.size(); 
            Integer allCardsToDeal = allCards.size()-1;

            Integer firstIndex = 0;
            Integer cardsPerPlayer = allCardsToDeal/totalPlayers;
            Collections.shuffle(allCards);

            // Ponemos cómo carta central la última de la baraja
            Integer lastCardIndex = allCards.size() - 1;
            currentGame.setCentral_card(allCards.get(lastCardIndex));

            for (GamePlayer gamePlayer : gamePlayers) {
                // Repartimos las cartas según la RN1 
                List<Card> playerCards = new ArrayList<>(allCards.subList(firstIndex, cardsPerPlayer));
                Hand playerHand = new Hand();
                playerHand.setCards(playerCards);
                handService.saveHand(playerHand);
                gamePlayer.setHand(playerHand);
                gamePlayerService.saveGamePlayer(gamePlayer);

                firstIndex += cardsPerPlayer;
                cardsPerPlayer += cardsPerPlayer;

            }
            currentGame.setStart(LocalDateTime.now());
        } catch (RuntimeException e) {
            System.err.println("Error al inicializar el juego: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error general al inicializar el juego.");
            e.printStackTrace();
        }
    }
}
