package com.dobble.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.List;
import java.util.Optional;

import com.dobble.model.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class PlayerRepositoryTest {

  @Autowired
  PlayerRepository pr;

  @Test
  public void testFindByUsername() {
    Optional<Player> o = pr.findByUsername("dobble");
    assumeTrue(o.isPresent());
    Player player = o.get();
    assertNotNull(player);
    assertEquals("dobble", player.getUsername());
  }

  @Test
  public void testFindByEmail() {
    Optional<Player> o = pr.findByEmail("dobble@example.com");
    assumeTrue(o.isPresent());
    Player player = o.get();
    assertNotNull(player);
    assertEquals("dobble@example.com", player.getEmail());
  }

  @Test
  public void testFindAllNonAdmin() {
    assertNotNull(pr);
    Optional<List<Player>> optionalPlayers = pr.findAllNonAdmin();
    assertTrue(optionalPlayers.isPresent(), "La lista de jugadores no puede ser nula.");
    List<Player> players = optionalPlayers.get();
    assertFalse(players.isEmpty(), "La lista de jugadores no puede estar vacía.");
    for (Player player : players) {
      assertFalse(player.getIs_admin(), "El jugador no debe ser administrador.");
    }
  }
}
