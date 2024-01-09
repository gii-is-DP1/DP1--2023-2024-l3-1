package org.springframework.samples.petclinic.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.GamePlayer;
import org.springframework.samples.petclinic.repositories.GamePlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class GamePlayerService {

  private final GamePlayerRepository gamePlayerRepository;

  @Autowired
  public GamePlayerService(GamePlayerRepository gamePlayerRepository) {

    this.gamePlayerRepository = gamePlayerRepository;
  }

  @Transactional
  public GamePlayer saveGamePlayer(@Valid GamePlayer gamePlayer) {
    this.gamePlayerRepository.save(gamePlayer);
    return gamePlayer;
  }


}
