package org.springframework.samples.petclinic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.Game;
import org.springframework.samples.petclinic.services.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/games")
@Tag(name = "Games", description = "The Games management API")
@SecurityRequirement(name = "bearerAuth")
public class GameController {

    private final GameService gameService; 

    @Autowired
    public GameController(GameService gameService){
        this.gameService= gameService; 
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> findGame(@PathVariable("id") String id ){
        Game gameToGet=gameService.findGame(id);
        if(gameToGet== null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Game>(gameToGet,HttpStatus.OK); 
    }


}
