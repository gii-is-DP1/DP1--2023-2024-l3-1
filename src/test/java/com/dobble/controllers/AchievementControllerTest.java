package com.dobble.controllers;

import com.dobble.controllers.AchievementController;
import com.dobble.model.Achievement;
import com.dobble.model.Player;
import com.dobble.model.enums.AchievementMetric;
import com.dobble.services.AchievementService;
import com.dobble.services.PlayerService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class AchievementControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AchievementService achievementService;

  @MockBean
  private PlayerService playerService;

  @InjectMocks
  private AchievementController achievementController;

  @Test
  @WithMockUser(username = "admin", password = "dobble_admin")
  void testFindAchievement() throws Exception {
    Achievement existingAchievement = new Achievement();
    existingAchievement.setId(10);
    existingAchievement.setName("Existing Achievement");
    existingAchievement.setDescription("Description for Existing Achievement");
    existingAchievement.setBadgeImage("Badge image link");
    existingAchievement.setThreshold(1.0);
    existingAchievement.setMetric(AchievementMetric.GAMES_PLAYED);

    when(achievementService.getAchievementById(10)).thenReturn(Optional.of(existingAchievement));

    mockMvc.perform(get("/api/achievements/10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("Existing Achievement")));
  }

  @Test
  @WithMockUser(username = "admin", password = "dobble_admin")
  void testFindNonExistingAchievement() throws Exception {
    when(achievementService.getAchievementById(200)).thenReturn(Optional.empty());

    mockMvc.perform(get("/api/achievements/200"))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(username = "admin", password = "dobble_admin")
  void testFindAllAchievements() throws Exception {
    Achievement achievement1 = new Achievement();
    achievement1.setId(12);
    achievement1.setName("Achievement 1");
    achievement1.setDescription("Description for Achievement 1");
    achievement1.setBadgeImage("Badge image link 1");
    achievement1.setThreshold(1.0);
    achievement1.setMetric(AchievementMetric.GAMES_PLAYED);

    Achievement achievement2 = new Achievement();
    achievement2.setId(13);
    achievement2.setName("Achievement 2");
    achievement2.setDescription("Description for Achievement 2");
    achievement2.setBadgeImage("Badge image link 2");
    achievement2.setThreshold(2.0);
    achievement2.setMetric(AchievementMetric.VICTORIES);

    List<Achievement> achievements = Arrays.asList(achievement1, achievement2);

    when(achievementService.getAchievements()).thenReturn(Optional.of(achievements));

    mockMvc.perform(get("/api/achievements"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].name", is("Achievement 1")))
        .andExpect(jsonPath("$[1].name", is("Achievement 2")));
  }

  @Test
  @WithMockUser(username = "admin", password = "dobble_admin")
  void testFindAllAchievementsEmptyList() throws Exception {
    when(achievementService.getAchievements()).thenReturn(Optional.empty());

    mockMvc.perform(get("/api/achievements"))
        .andExpect(status().isNoContent());
  }

  // @Test
  // @WithMockUser(username = "admin", password = "dobble_admin")
  // void testCreateAchievement() throws Exception {
  // Achievement newAchievement = new Achievement();
  // newAchievement.setId(10);
  // newAchievement.setName("New Achievement");
  // newAchievement.setDescription("Description for New Achievement");
  // newAchievement.setBadgeImage("Badge image link");
  // newAchievement.setThreshold(1.0);
  // newAchievement.setMetric(AchievementMetric.GAMES_PLAYED);

  // when(achievementService.saveAchievement(any(Achievement.class))).thenReturn(newAchievement);

  // mockMvc.perform(post("/api/achievements")
  // .contentType(MediaType.APPLICATION_JSON)
  // .content("{\"name\": \"New Achievement\"}"))
  // .andExpect(status().isCreated())
  // .andExpect(jsonPath("$.name", is("New Achievement")));
  // }

  // @Test
  // @WithMockUser(username = "admin", password = "dobble_admin")
  // void testModifyAchievement() throws Exception {
  // Player adminUser = new Player();
  // adminUser.setIs_admin(true);
  // when(playerService.findCurrentPlayer()).thenReturn(Optional.of(adminUser));

  // Achievement existingAchievement = new Achievement();
  // existingAchievement.setId(12);
  // existingAchievement.setName("Existing Achievement");
  // existingAchievement.setDescription("Description for Existing Achievement");
  // existingAchievement.setBadgeImage("Badge image link");
  // existingAchievement.setThreshold(1.0);
  // existingAchievement.setMetric(AchievementMetric.GAMES_PLAYED);
  // when(achievementService.getAchievementById(1)).thenReturn(Optional.of(existingAchievement));

  // Achievement updatedAchievement = new Achievement();
  // updatedAchievement.setId(12);
  // updatedAchievement.setName("Updated Achievement");
  // updatedAchievement.setDescription("Updated Description");
  // updatedAchievement.setBadgeImage("Updated Badge Image");
  // updatedAchievement.setThreshold(2.0);
  // updatedAchievement.setMetric(AchievementMetric.VICTORIES);

  // when(achievementService.updateAchievement(any(Achievement.class),
  // eq(1))).thenReturn(updatedAchievement);

  // mockMvc.perform(patch("/api/achievements/12")
  // .contentType(MediaType.APPLICATION_JSON)
  // .content(
  // "{\"name\": \"Updated Achievement\", \"description\": \"Updated
  // Description\", \"badgeImage\": \"Updated Badge Image\", \"threshold\": 2.0,
  // \"metric\": \"VICTORIES\"}"))
  // .andExpect(status().isOk())
  // .andExpect(jsonPath("$.name", is("Updated Achievement")))
  // .andExpect(jsonPath("$.description", is("Updated Description")))
  // .andExpect(jsonPath("$.badgeImage", is("Updated Badge Image")))
  // .andExpect(jsonPath("$.threshold", is(2.0)))
  // .andExpect(jsonPath("$.metric", is("VICTORIES")));
  // }

  // @Test
  // @WithMockUser(username = "admin", password = "dobble_admin")
  // void testDeleteAchievement() throws Exception {
  // Achievement existingAchievement = new Achievement();
  // existingAchievement.setId(10);
  // existingAchievement.setName("New Achievement");
  // existingAchievement.setDescription("Description for New Achievement");
  // existingAchievement.setBadgeImage("Badge image link");
  // existingAchievement.setThreshold(1.0);
  // existingAchievement.setMetric(AchievementMetric.GAMES_PLAYED);
  // achievementService.saveAchievement(existingAchievement);

  // when(achievementService.getAchievementById(10)).thenReturn(Optional.of(existingAchievement));

  // mockMvc.perform(delete("/api/achievements/10"))
  // .andExpect(status().isNoContent());
  // }

}
