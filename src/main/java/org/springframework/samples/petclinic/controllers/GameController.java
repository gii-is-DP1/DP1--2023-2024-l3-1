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
import org.springframework.samples.petclinic.model.enums.GameState;
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
    public GameController(GameService gameService,PlayerService playerService){
        this.gameService= gameService; 
        this.playerService = playerService; 
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> findGame(@PathVariable("id") String id ){
        Game gameToGet=gameService.findGame(id);
        if(gameToGet== null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Game>(gameToGet,HttpStatus.OK); 
    }

    //Crear una partida nueva con su nombre y máximo numero de jugadores
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Game> createGame(@Valid @RequestBody GameCreateDto gameCreateDTO){
        try {
            Optional<Player> currentPlayer= playerService.findCurrentPlayer();
            if (currentPlayer.isPresent()) {
                Game game= new Game(); 
                Set<Player> ls= Set.of(currentPlayer.get()); 
                
                game.setName(gameCreateDTO.getName()); 
                game.setStart(LocalDateTime.now());
                game.setCreator(currentPlayer.get());
                game.setPlayers(ls);
                gameService.saveGame(game);
                return new ResponseEntity<>(game, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
            }
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }  
    }

    //Cambiar el número máximo de usuarios de una partida en el lobby
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Game> updateGameLobby(@Valid @RequestBody GameCreateDto gameCreateDTO,@PathVariable("id") String id ){
        Game currentGame= gameService.findGame(id);
        Game newGame;
        if ( currentGame != null){
            Optional<Player> currentPlayer= playerService.findCurrentPlayer(); 
            Player creator= currentGame.getCreator();
            if (currentPlayer.isPresent()) {
                if(currentPlayer.get().equals(creator) && currentGame.isOnLobby()){
                    newGame= gameService.updateGame(gameCreateDTO, id); 
                   
                }else{
                     return new ResponseEntity<>(HttpStatus.CONFLICT); 
                }

            }else{
                 return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
            }
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
            
        return new ResponseEntity<>(newGame,HttpStatus.OK); 
        

    }

    //Unirse a partida
    @PostMapping("/join/{gameId}")
    public ResponseEntity<Game> joinGame(@PathVariable String gameId) {
        Game gameToJoin = gameService.findGame(gameId);
        Game savedGame;
        if (gameToJoin != null && gameToJoin.isOnLobby()) {
            Optional<Player> currentPlayer = playerService.findCurrentPlayer();
            if (currentPlayer.isPresent()) {
                if (!gameToJoin.getPlayers().contains(currentPlayer.get())) {
                    if (gameToJoin.getMaxPlayers() >= gameToJoin.getPlayers().size()) {
                        Player joiningPlayer = currentPlayer.get();
                        gameToJoin.getPlayers().add(joiningPlayer);
                        savedGame = gameService.saveGame(gameToJoin);
                    } else {
                        // Error 409
                        return new ResponseEntity<>(HttpStatus.CONFLICT);
                    }
                } else {
                    // Error 409 Ya se encuentra en la partida
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
            } else {
                // Error 404
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            if (gameToJoin == null)
                // Error 404 Lanza una excepción si el juego no está en el estado correcto para unirse
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            else
                // Error 409
                return new ResponseEntity<>(HttpStatus.CONFLICT);

        }
        return new ResponseEntity<>(savedGame, HttpStatus.OK);
    }

    //Iniciar partida
    @PostMapping("/start/{gameId}")
    public ResponseEntity<Game> startGame(@PathVariable String gameId) {
        Game currentGame= gameService.findGame(gameId);
        Player creator= currentGame.getCreator();
        Game savedGame; 
        if (currentGame!=null && currentGame.isOnLobby()){
            Optional<Player> currentPlayer = playerService.findCurrentPlayer(); 
            if (currentPlayer.isPresent()) {
                if(currentPlayer.get().equals(creator) && currentGame.getPlayers().size()>=2){
                    currentGame.setGame_state(GameState.ONGOING);
                    savedGame=gameService.saveGame(currentGame);
                }else{
                    return new ResponseEntity<>(HttpStatus.CONFLICT); 
                }
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(savedGame,HttpStatus.OK); 
    }




}
