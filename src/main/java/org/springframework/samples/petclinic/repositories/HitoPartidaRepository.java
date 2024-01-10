package org.springframework.samples.petclinic.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Game;
import org.springframework.samples.petclinic.model.HitoPartida;

public interface HitoPartidaRepository extends CrudRepository<HitoPartida, Integer> {
  List<HitoPartida> findAll();

  public Optional<HitoPartida> findByGame(@Param("game") Game game);

}
