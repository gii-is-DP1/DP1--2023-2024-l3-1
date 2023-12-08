package org.springframework.samples.petclinic.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Game;

public interface GameRepository extends CrudRepository<Game, String> {
    List<Game> findAll();

    public Optional<Game> findByName(@Param("name") String name);
}
