package org.springframework.samples.petclinic.services;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.model.Player;
import org.springframework.samples.petclinic.repositories.PlayerRepository;

@DataJpaTest
public class PlayerRepositoryTest {

  @Autowired
  PlayerRepository pr;

  @Test
  public void findByUsername() {
    Optional<Player> o = pr.findByUsername("dobble");
    assumeTrue(o.isPresent());
    Player player = o.get();
    assertNotNull(player);
    assertEquals("dobble", player.getUsername());
  }

  @Test
  public void findByEmail() {
    Optional<Player> o = pr.findByEmail("dobble@example.com");
    assumeTrue(o.isPresent());
    Player player = o.get();
    assertNotNull(player);
    assertEquals("dobble@example.com", player.getEmail());
  }

}
