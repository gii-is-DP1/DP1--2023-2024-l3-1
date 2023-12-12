package org.springframework.samples.petclinic.services.achievements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Achievement;
import org.springframework.samples.petclinic.repositories.AchievementRepository;

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
      Achievement achievement = achievementRepository.findByName("Jugador Veterano");
      assertNotNull(achievement);
      assertEquals("Jugador Veterano", achievement.getName());
  }
}
