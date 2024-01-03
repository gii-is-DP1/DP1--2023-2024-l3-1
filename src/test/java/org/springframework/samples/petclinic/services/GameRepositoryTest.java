package org.springframework.samples.petclinic.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Game;
import org.springframework.samples.petclinic.repositories.AchievementRepository;
import org.springframework.samples.petclinic.repositories.GameRepository;

@SpringBootTest
@AutoConfigureTestDatabase
public class GameRepositoryTest {

  @Autowired
  private AchievementRepository achievementRepository;

  @Autowired
  private GameRepository gameRepository;

  @Test
  void testRepositoryFindAll() {
    List<Game> games = gameRepository.findAll();
    assertNotNull(games);
  }

  @Test
  void testFindByName() {
    Optional<Game> game = gameRepository.findByName("partida1");
    assertNotNull(game);
    assertEquals("partida1", game.get().getName());
  }
}
