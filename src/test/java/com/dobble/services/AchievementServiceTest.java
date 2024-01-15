package com.dobble.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import com.dobble.model.Achievement;
import com.dobble.model.enums.AchievementMetric;
import com.dobble.services.AchievementService;

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
    @Transactional
    void testServiceDeleteAchievementById() {
        Optional<Achievement> achievementToDelete = achievementService.getAchievementById(9);
        assertTrue(achievementToDelete.isPresent());
        achievementService.deleteAchievementById(9);
        Optional<Achievement> achievementAfterDeletion = achievementService.getAchievementById(9);
        assertFalse(achievementAfterDeletion.isPresent());
    }

    // @Test
    // void testServiceGetAchievementByName() {
    // Optional<Achievement> achievement=
    // achievementService.getAchievementByName("Campeón Dobble");
    // assertNotNull(achievement);
    // assertEquals("Campeón Dobble", achievement.getName());
    // }

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
