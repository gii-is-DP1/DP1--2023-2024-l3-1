package org.springframework.samples.petclinic.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.GamePlayer;
import org.springframework.samples.petclinic.model.GamePlayerId;

public interface GamePlayerRepository extends CrudRepository<GamePlayer,GamePlayerId>{
  
}
