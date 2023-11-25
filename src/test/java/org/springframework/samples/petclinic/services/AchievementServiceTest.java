package org.springframework.samples.petclinic.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Achievement;
import org.springframework.samples.petclinic.model.Metric;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase
public class AchievementServiceTest {

    @Autowired
    private AchievementService achievementService; 

    @Test
    void testGetAchievements() {
        List<Achievement> achievements= achievementService.getAchievements(); 
        assertNotNull(achievements);
        assertTrue(achievements.size() > 0);
    }

    @Test
    void testGetById() {
        Achievement achievement= achievementService.getById(1);
        assertNotNull(achievement);
        assertEquals("logro1", achievement);
    }

    @Test
    @Transactional
    void testSaveAchievement() {
        Achievement newAchievement= new Achievement(); 
        newAchievement.setName("New Achievement");
        newAchievement.setDescription("Description for New Achievement");
        newAchievement.setMetric(Metric.GAMES_PLAYED);
        achievementService.saveAchievement(newAchievement);

        Achievement achievement= achievementService.getAchievementByName("New Achievement"); 

        assertNotNull(achievement);
        assertEquals("New Achievement",achievement.getName());
    }

    @Test
    @Transactional
    void testDeleteAchievementById() {
        Achievement achievementToDelete= achievementService.getById(1);
        assertNotNull(achievementToDelete);
        achievementService.deleteAchievementById(1);
        Achievement achievement = achievementService.getById(1);
        assertNull(achievement);
        
    }

    @Test
    void testGetAchievementByName() {
        Achievement achievement= achievementService.getAchievementByName("logro1");  
        assertNotNull(achievement);
        assertEquals("logro1", achievement);
    }

    
}
