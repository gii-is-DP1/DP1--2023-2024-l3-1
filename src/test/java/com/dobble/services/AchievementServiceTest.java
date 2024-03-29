package com.dobble.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import com.dobble.model.Achievement;
import com.dobble.model.enums.AchievementMetric;
import com.dobble.repositories.AchievementRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase
public class AchievementServiceTest {

    @Autowired
    private AchievementService achievementService;

    @Test
    public void serviceExists() {
        assertNotNull(achievementService);
    }

    @Test
    void testServiceGetAchievements() {
        Optional<List<Achievement>> optionalAchievements = achievementService.getAchievements();
        assertTrue(optionalAchievements.isPresent());
        List<Achievement> achievements = optionalAchievements.orElse(Collections.emptyList());
        assertNotNull(achievements);
        assertTrue(achievements.size() > 0);
    }

    @Test
    void testServiceGetById() {
        Optional<Achievement> achievement = achievementService.getAchievementById(9);
        assertTrue(achievement.isPresent());
        assertEquals("Iniciador", achievement.get().getName());
    }

    @Test
    void testServiceGetByIdNotFound() {
        Optional<Achievement> achievement = achievementService.getAchievementById(12345);
        assertFalse(achievement.isPresent());
    }

    @Test
    @Transactional
    void testServiceSaveAchievement() {
        Achievement newAchievement = new Achievement();
        newAchievement.setName("New Achievement");
        newAchievement.setDescription("Description for New Achievement");
        newAchievement.setBadgeImage("Badge image link");
        newAchievement.setThreshold(1.0);
        newAchievement.setMetric(AchievementMetric.GAMES_PLAYED);
        achievementService.saveAchievement(newAchievement);
        Optional<Achievement> optionalAchievement = achievementService.getAchievementByName("New Achievement");
        assertTrue(optionalAchievement.isPresent());
        Achievement achievement = optionalAchievement.get();
        assertNotNull(achievement);
        assertEquals("New Achievement", achievement.getName());
    }

    @Test
    public void testUpdateAchievement() {
        AchievementRepository mockRepository = mock(AchievementRepository.class);

        AchievementService achievementService = new AchievementService(mockRepository);

        Achievement existingAchievement = new Achievement();
        existingAchievement.setId(1);
        existingAchievement.setName("Existing Achievement");
        existingAchievement.setDescription("Description");
        existingAchievement.setBadgeImage("Image");
        existingAchievement.setThreshold(10.0);
        existingAchievement.setMetric(AchievementMetric.GAMES_PLAYED);

        when(mockRepository.findById(1)).thenReturn(Optional.of(existingAchievement));

        Achievement updatedAchievement = new Achievement();
        updatedAchievement.setName("Updated Achievement");
        updatedAchievement.setDescription("Updated Description");
        updatedAchievement.setBadgeImage("Updated Image");
        updatedAchievement.setThreshold(20.0);
        updatedAchievement.setMetric(AchievementMetric.GAMES_PLAYED);

        Achievement result = achievementService.updateAchievement(updatedAchievement, 1);

        assertNotNull(result);
        assertEquals("Updated Achievement", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertEquals("Updated Image", result.getBadgeImage());
        assertEquals(AchievementMetric.GAMES_PLAYED, result.getMetric());
    }

    @Test
    @Transactional
    void testServiceDeleteAchievementById() {
        Optional<Achievement> achievementToDelete = achievementService.getAchievementById(9);
        assertTrue(achievementToDelete.isPresent());
        achievementService.deleteAchievementById(9);
        Optional<Achievement> achievementAfterDeletion = achievementService.getAchievementById(9);
        assertFalse(achievementAfterDeletion.isPresent());
    }

    @Test
    void testServiceGetAchievementByName() {
        Optional<Achievement> optionalAchievement = achievementService.getAchievementByName("Entrando en Calor");
        assertTrue(optionalAchievement.isPresent());
        Achievement achievement = optionalAchievement.orElseThrow();
        assertNotNull(achievement);
        assertEquals("Entrando en Calor", achievement.getName());
    }

    @Test
    void testServiceGetAchievementById() {
        Optional<Achievement> optionalAchievement = achievementService.getAchievementById(6);
        assertTrue(optionalAchievement.isPresent());
        Achievement achievement = optionalAchievement.get();
        assertNotNull(achievement);
        assertEquals("Master del Tiempo", achievement.getName());
    }

}
