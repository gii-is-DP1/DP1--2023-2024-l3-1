package org.springframework.samples.petclinic.repositories;

import org.springframework.samples.petclinic.model.Player;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PlayerRepository extends CrudRepository<Player, Integer> {
    @Query("SELECT DISTINCT p FROM Player p WHERE p.username = :username")
	public Optional<Player> findByUsername(@Param("username") String username);

    @Query("SELECT DISTINCT p FROM Player p WHERE p.is_admin = true ORDER BY p.username")
    public Optional<List<Player>> findAllNonAdmin();
}
