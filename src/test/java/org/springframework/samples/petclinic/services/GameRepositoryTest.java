package org.springframework.samples.petclinic.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Game;
import org.springframework.samples.petclinic.repositories.GameRepository;

@SpringBootTest
@AutoConfigureTestDatabase
public class GameRepositoryTest {

  @Autowired
  private GameRepository gameRepository;

  @Test
  public void testRepositoryFindAll() {
    List<Game> achievements = gameRepository.findAll();
    assertNotNull(achievements);
  }

}
