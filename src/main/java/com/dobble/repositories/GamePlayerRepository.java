package com.dobble.repositories;

import java.util.List;
import java.util.Optional;

import com.dobble.model.GamePlayer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface GamePlayerRepository extends CrudRepository<GamePlayer, Integer> {
  @Query("SELECT DISTINCT gp FROM Player p JOIN p.game_players gp WHERE p.username = :username")
  public Optional<List<GamePlayer>> findGamePlayerByUsername(@Param("username") String username);

  @Query("SELECT DISTINCT gp FROM Player p JOIN p.game_players gp WHERE p.username = :username AND gp.game.id = :game_id")
  public Optional<GamePlayer> findGamePlayerByUsernameAndGame(@Param("username") String username,
      @Param("game_id") String game_id);
}
