package com.dobble;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import com.dobble.model.Achievement;
import com.dobble.repositories.AchievementRepository;

@SpringBootTest
@AutoConfigureTestDatabase
public class AchievementRepositoryTest {

  @Autowired
  private AchievementRepository achievementRepository;

  @Test
  void testRepositoryFindAll() {
    List<Achievement> achievements = achievementRepository.findAll();
    assertNotNull(achievements);
  }

  @Test
  void testFindByName() {
    Optional<Achievement> optionalAchievement = achievementRepository.findByName("Jugador Veterano");
    assertTrue(optionalAchievement.isPresent());
    Achievement achievement = optionalAchievement.orElseThrow();
    assertNotNull(achievement);
    assertEquals("Jugador Veterano", achievement.getName());
  }
}
