package com.dobble.repositories;

import java.util.List;
import java.util.Optional;

import com.dobble.model.Game;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends CrudRepository<Game, String> {
    List<Game> findAll();

    public Optional<Game> findByName(@Param("name") String name);

    @Query("SELECT DISTINCT gp.game FROM Player p JOIN p.game_players gp WHERE p.id = :playerId")
    public Optional<List<Game>> findGamesByPlayerId(@Param("playerId") Integer playerId);

    @Query("SELECT DISTINCT gp.game FROM Player p JOIN p.game_players gp WHERE p.username = :username")
    public Optional<List<Game>> findGamesByPlayerUsername(@Param("username") String username);
}
