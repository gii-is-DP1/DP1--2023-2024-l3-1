package org.springframework.samples.petclinic.services;
// ¡NO BORRAR ESTE ARCHIVO! || FUNCIONABA Y HE TOCADO ALGO Y HA DEJADO DE IR || EN ARREGLO

// package org.springframework.samples.petclinic.services.achievements;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.samples.petclinic.model.Achievement;
// import org.springframework.samples.petclinic.model.Player;
// import org.springframework.samples.petclinic.services.AchievementService;
// import org.springframework.samples.petclinic.services.PlayerService;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.samples.petclinic.model.enums.AchievementMetric;
// import static org.hamcrest.Matchers.is;

// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// import java.util.Optional;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.eq;

// import static org.mockito.Mockito.when;

// @SpringBootTest
// @AutoConfigureMockMvc
// class AchievementControllerTest {

//   @Autowired
//   private MockMvc mockMvc;

//   @MockBean
//   private AchievementService achievementService;

//   @MockBean
//     private PlayerService playerService;

//   @Test
//   @WithMockUser(username = "admin", password = "dobble_admin")
//   void testFindAchievement() throws Exception {
//     Achievement newAchievement = new Achievement(); 
//         newAchievement.setId(10);
//         newAchievement.setName("New Achievement");
//         newAchievement.setDescription("Description for New Achievement");
//         newAchievement.setBadgeImage("Badge image link");
//         newAchievement.setThreshold(1.0);
//         newAchievement.setMetric(AchievementMetric.GAMES_PLAYED);
//         achievementService.saveAchievement(newAchievement);

//     when(achievementService.getAchievementByName("New Achievement")).thenReturn(newAchievement);

//     mockMvc.perform(get("/api/v1/achievements/10"))
//         .andExpect(status().isOk())
//         .andExpect(jsonPath("$.name", is("New Achievement")));
//   }

//   @Test
//   @WithMockUser(username = "admin", password = "dobble_admin")
//   void testCreateAchievement() throws Exception {
//     Achievement newAchievement = new Achievement(); 
//         newAchievement.setId(10);
//         newAchievement.setName("New Achievement");
//         newAchievement.setDescription("Description for New Achievement");
//         newAchievement.setBadgeImage("Badge image link");
//         newAchievement.setThreshold(1.0);
//         newAchievement.setMetric(AchievementMetric.GAMES_PLAYED);
//         achievementService.saveAchievement(newAchievement);

//     when(achievementService.saveAchievement(any(Achievement.class))).thenReturn(newAchievement);

//     mockMvc.perform(post("/api/v1/achievements/new")
//         .contentType(MediaType.APPLICATION_JSON)
//         .content("{\"name\": \"New Achievement\"}"))
//         .andExpect(status().isCreated())
//         .andExpect(jsonPath("$.name", is("New Achievement")));
//   }

//   @Test
//   @WithMockUser(username = "admin", password = "dobble_admin")
//   void testModifyAchievement() throws Exception {
//     Achievement existingAchievement = new Achievement(); 
//         existingAchievement.setId(10);
//         existingAchievement.setName("New Achievement old");
//         existingAchievement.setDescription("Description for New Achievement");
//         existingAchievement.setBadgeImage("Badge image link");
//         existingAchievement.setThreshold(1.0);
//         existingAchievement.setMetric(AchievementMetric.GAMES_PLAYED);
//         achievementService.saveAchievement(existingAchievement);
//       Achievement updatedAchievement = new Achievement(); 
//         updatedAchievement.setName("New Achievement updated");
//         achievementService.saveAchievement(updatedAchievement);

//     when(playerService.findCurrentPlayer()).thenReturn(Optional.of(new Player()));
//     when(achievementService.getAchievementById(1)).thenReturn(Optional.of(existingAchievement));
//     when(achievementService.updateAchievement(any(Achievement.class), eq(1))).thenReturn(updatedAchievement);

//     mockMvc.perform(patch("/api/v1/achievements/1")
//         .contentType(MediaType.APPLICATION_JSON)
//         .content("{\"name\": \"Updated Achievement\"}"))
//         .andExpect(status().isOk())
//         .andExpect(jsonPath("$.name", is("Updated Achievement")));
//   }

//   @Test
//   @WithMockUser(username = "admin", password = "dobble_admin")
//   void testDeleteAchievement() throws Exception {
//     Achievement existingAchievement = new Achievement(); 
//         existingAchievement.setId(10);
//         existingAchievement.setName("New Achievement");
//         existingAchievement.setDescription("Description for New Achievement");
//         existingAchievement.setBadgeImage("Badge image link");
//         existingAchievement.setThreshold(1.0);
//         existingAchievement.setMetric(AchievementMetric.GAMES_PLAYED);
//         achievementService.saveAchievement(existingAchievement);

//     when(achievementService.getAchievementById(1)).thenReturn(existingAchievement);

//     mockMvc.perform(delete("/api/v1/achievements/10"))
//         .andExpect(status().isNoContent());
//   }
// }

// ¡NO BORRAR ESTE ARCHIVO! || FUNCIONABA Y HE TOCADO ALGO Y HA DEJADO DE IR || EN ARREGLO
