package com.dobble.repositories;

import java.util.List;
import java.util.Optional;

import com.dobble.model.Player;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PlayerRepository extends CrudRepository<Player, Integer> {
    @Query("SELECT DISTINCT p FROM Player p WHERE p.username = :username")
    public Optional<Player> findByUsername(@Param("username") String username);

    @Query("SELECT DISTINCT p FROM Player p WHERE p.email = :email")
    public Optional<Player> findByEmail(@Param("email") String email);

    @Query("SELECT DISTINCT p FROM Player p WHERE p.is_admin = false ORDER BY p.username")
    public Optional<List<Player>> findAllNonAdmin();
}
