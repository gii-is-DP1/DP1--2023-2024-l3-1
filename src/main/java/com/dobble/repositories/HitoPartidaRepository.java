package com.dobble.repositories;

import java.util.List;
import java.util.Optional;

import com.dobble.model.Game;
import com.dobble.model.HitoPartida;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface HitoPartidaRepository extends CrudRepository<HitoPartida, Integer> {
  List<HitoPartida> findAll();

  public Optional<HitoPartida> findByGame(@Param("game") Game game);

}
