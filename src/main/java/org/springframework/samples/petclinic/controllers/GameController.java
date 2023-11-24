package org.springframework.samples.petclinic.controllers;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.Game;
import org.springframework.samples.petclinic.model.Player;
import org.springframework.samples.petclinic.services.GameService;
import org.springframework.samples.petclinic.services.PlayerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Game>(gameToGet,HttpStatus.OK); 
    }

    @PostMapping
    public ResponseEntity<Game> createGame(@Valid @RequestBody Game game){
        try {
            Optional<Player> currentPlayer= playerService.findCurrentPlayer();
            if (currentPlayer.isPresent()) {
                Set<Player> ls= Set.of(currentPlayer.get()); 

                game.setStart(LocalDateTime.now());
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


}
