package org.springframework.samples.petclinic.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Game;
import org.springframework.samples.petclinic.repositories.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class GameService {
    private final GameRepository gameRepository; 

    @Autowired
    public GameService(GameRepository gameRepository){
        this.gameRepository= gameRepository; 
    }   
    @Transactional(readOnly = true)
    public Game findGame(String id){
        Optional<Game> game = gameRepository.findById(id);
        return game.isPresent()?game.get():null;
    }

    @Transactional(readOnly = true)
	public Optional<Game> findByNameGame(String name) {
		return gameRepository.findByName(name);
	}

    @Transactional()
    public Game saveGame(@Valid Game game){
        this.gameRepository.save(game); 
        return game; 
    }

}
