package com.dobble.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.dobble.configuration.jwt.JwtUtils;
import com.dobble.configuration.services.UserDetailsImpl;
import com.dobble.controllers.PlayerController;
import com.dobble.dto.EditPlayerDto;
import com.dobble.dto.JwtResponseDto;
import com.dobble.dto.LoginRequestDto;
import com.dobble.dto.PublicPlayerDto;
import com.dobble.dto.SignupRequestDto;
import com.dobble.model.Player;
import com.dobble.model.enums.Icon;
import com.dobble.services.PlayerService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for {@link PlayerController}
 *
 */

// @WebMvcTest(value = PlayerController.class, excludeFilters =
// @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes =
// WebSecurityConfigurer.class), excludeAutoConfiguration = {
// SecurityAutoConfiguration.class })
@SpringBootTest
@AutoConfigureMockMvc
public class PlayerControllerTest {

  private static final int TEST_PLAYER_ID = 1;
  private static final int TEST_PLAYER_NON_ADMIN_ID = 2;
  private static final String BASE_URL = "/api/player";

  @SuppressWarnings("unused")
  @Autowired
  private PlayerController playerController;

  @MockBean
  private AuthenticationManager authenticationManager;

  @MockBean
  private JwtUtils jwtUtils;

  @MockBean
  private PlayerService playerService;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  private LoginRequestDto loginRequest;
  private SignupRequestDto signupRequest;
  private UserDetailsImpl userDetails;
  private String token;
  private String username;
  private String password;

  private Player playerTest;
  private Player playerTestNonAdmin;

  @BeforeEach
  void setup() {
    playerTest = new Player();
    playerTest.setId(TEST_PLAYER_ID);
    playerTest.setUsername("PlayerTest");
    playerTest.setPassword("passwordTest");
    playerTest.setEmail("playerTest@email.com");
    playerTest.setProfile_icon(Icon.AGUA);
    playerTest.setIs_admin(true);

    playerTestNonAdmin = new Player();
    playerTestNonAdmin.setId(TEST_PLAYER_NON_ADMIN_ID);
    playerTestNonAdmin.setUsername("PlayerNonAdmin");
    playerTestNonAdmin.setPassword("passwordNonAdmin");
    playerTestNonAdmin.setEmail("playerNonAdmin@email.com");
    playerTestNonAdmin.setProfile_icon(Icon.AGUA);
    playerTestNonAdmin.setIs_admin(false);

    given(this.playerService.findPlayer(TEST_PLAYER_ID)).willReturn(Optional.of(playerTest));
    given(this.playerService.findPlayer(TEST_PLAYER_NON_ADMIN_ID)).willReturn(Optional.of(playerTestNonAdmin));

    String username = "player";
    String password = "password";
    loginRequest = new LoginRequestDto();
    loginRequest.setUsername(username);
    loginRequest.setPassword(password);

    signupRequest = new SignupRequestDto();
    signupRequest.setUsername(username);
    signupRequest.setPassword(password);
    signupRequest.setEmail("playertest@email.com");
    signupRequest.setProfile_icon(Icon.AGUA);

    token = "JWT TOKEN";
    JwtResponseDto jwtResponseDto = new JwtResponseDto(token, 1, username, true);

    Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
    UserDetailsImpl userDetails = new UserDetailsImpl(1, username, password,
        List.of(new SimpleGrantedAuthority("ADMIN")));
  }

  // Test authenticateUser
  @Test
  void shouldAuthenticateUser_WhenAuthenticationFails() {
    LoginRequestDto loginRequest = new LoginRequestDto();
    loginRequest.setUsername("invalid_username");
    loginRequest.setPassword("invalid_password");
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(BadCredentialsException.class);

    ResponseEntity<?> responseEntity = playerController.authenticateUser(loginRequest);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }
  // Test getMe

  // Test validateToken
  @Test
  void shouldValidateToken() {
    String validToken = "validToken";
    when(jwtUtils.validateJwtToken(validToken)).thenReturn(true);

    ResponseEntity<?> responseEntity = playerController.validateToken(validToken);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }

  @Test
  void shouldValidateToken_WhenTokenIsInvalid() {
    String invalidToken = "invalidToken";
    when(jwtUtils.validateJwtToken(invalidToken)).thenReturn(false);

    ResponseEntity<?> responseEntity = playerController.validateToken(invalidToken);

    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
  }

  // Test getMe
  @Test
  @WithMockUser(username = "PlayerTest", password = "passwordTest")
  void shouldReturnCurrentUser() throws Exception {
    given(this.playerService.findCurrentPlayer()).willReturn(Optional.of(playerTest));

    mockMvc.perform(get(BASE_URL + "/me")
        .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(playerTest.getId()))
        .andExpect(jsonPath("$.username").value(playerTest.getUsername()));

  }

  @Test
  @WithMockUser(username = "PlayerTest", password = "passwordTest")
  void shouldReturnUnauthorizedWhenNoUserIsLoggedIn() throws Exception {
    given(this.playerService.findCurrentPlayer()).willReturn(Optional.empty());
    mockMvc.perform(get(BASE_URL + "/me"))
        .andExpect(status().isUnauthorized());

  }

  // Test editUser
  @Test
  void shouldEditUser() {
    Player adminUser = new Player();
    adminUser.setIs_admin(true);
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(adminUser));

    Player targetUser = new Player();
    targetUser.setId(1);
    when(playerService.findPlayer(anyInt())).thenReturn(Optional.of(targetUser));

    EditPlayerDto editPlayerDto = new EditPlayerDto();
    when(playerService.existsUser(targetUser, editPlayerDto)).thenReturn(false);

    Player updatedPlayer = new Player();
    when(playerService.updatePlayer(editPlayerDto, targetUser.getId())).thenReturn(updatedPlayer);

    ResponseEntity<?> responseEntity = playerController.editUser(1, editPlayerDto);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(updatedPlayer, responseEntity.getBody());
  }

  @Test
  void shouldEditUser_WhenTargetUserNotPresent() {
    Player adminUser = new Player();
    adminUser.setIs_admin(true);
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(adminUser));

    when(playerService.findPlayer(anyInt())).thenReturn(Optional.empty());

    ResponseEntity<?> responseEntity = playerController.editUser(1, new EditPlayerDto());

    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    verify(playerService, never()).updatePlayer(any(), anyInt());
  }

  @Test
  void shouldEditUser_WhenCurrentUserNotAdmin() {
    Player nonAdminUser = new Player();
    nonAdminUser.setIs_admin(false);
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(nonAdminUser));

    Player targetUser = new Player();
    when(playerService.findPlayer(anyInt())).thenReturn(Optional.of(targetUser));

    ResponseEntity<?> responseEntity = playerController.editUser(1, new EditPlayerDto());

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    verify(playerService, never()).updatePlayer(any(), anyInt());
  }

  @Test
  void shouldEditUser_WhenEditedUserAlreadyExists() {
    Player adminUser = new Player();
    adminUser.setIs_admin(true);
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(adminUser));

    Player targetUser = new Player();
    when(playerService.findPlayer(anyInt())).thenReturn(Optional.of(targetUser));

    EditPlayerDto editPlayerDto = new EditPlayerDto();
    when(playerService.existsUser(targetUser, editPlayerDto)).thenReturn(true);

    ResponseEntity<?> responseEntity = playerController.editUser(1, editPlayerDto);

    assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    verify(playerService, never()).updatePlayer(any(), anyInt());
  }

  // Test editMe
  @Test
  void shouldEditMe() {
    Player currentUser = new Player();
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(currentUser));

    EditPlayerDto editPlayerDto = new EditPlayerDto();
    when(playerService.existsUser(currentUser, editPlayerDto)).thenReturn(false);

    Player updatedPlayer = new Player();
    when(playerService.updatePlayer(editPlayerDto, currentUser.getId())).thenReturn(updatedPlayer);

    ResponseEntity<?> responseEntity = playerController.editMe(editPlayerDto);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(updatedPlayer, responseEntity.getBody());
  }

  @Test
  void shouldEditMe_WhenCurrentUserNotPresent() {
    when(playerService.findCurrentPlayer()).thenReturn(Optional.empty());

    ResponseEntity<?> responseEntity = playerController.editMe(new EditPlayerDto());

    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    verify(playerService, never()).updatePlayer(any(), anyInt());
  }

  // Test deleteUser
  @Test
  void shouldDeleteUser_WhenTargetUserNotFound() {
    Player currentUser = new Player();
    currentUser.setIs_admin(true);
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(currentUser));

    when(playerService.findPlayer(1)).thenReturn(Optional.empty());

    ResponseEntity<?> responseEntity = playerController.deleteUser(1);

    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    verify(playerService, never()).deletePlayer(anyInt());
  }

  @Test
  void shouldDeleteUser_WhenCurrentUserNotAdmin() {
    Player currentUser = new Player();
    currentUser.setIs_admin(false);
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(currentUser));

    Player targetUser = new Player();
    when(playerService.findPlayer(1)).thenReturn(Optional.of(targetUser));

    ResponseEntity<?> responseEntity = playerController.deleteUser(1);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    verify(playerService, never()).deletePlayer(anyInt());
  }

  @Test
  void shouldDeleteUser_WhenUserDeletedSuccessfully() {
    Player currentUser = new Player();
    currentUser.setIs_admin(true);
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(currentUser));

    Player targetUser = new Player();
    when(playerService.findPlayer(1)).thenReturn(Optional.of(targetUser));

    ResponseEntity<?> responseEntity = playerController.deleteUser(1);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    verify(playerService, times(1)).deletePlayer(1);
  }

  // Test addFriendToUser
  @Test
  @WithMockUser(username = "admin", password = "dobble_admin")
  void shouldAddFriendToUser_WithValidAdminUser() throws Exception {
    Player adminPlayer = new Player();
    adminPlayer.setId(1);
    adminPlayer.setIs_admin(true);

    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(adminPlayer));

    Player targetPlayer = new Player();
    targetPlayer.setId(2);
    targetPlayer.setIs_admin(false);

    when(playerService.findPlayer(anyInt())).thenReturn(Optional.of(targetPlayer));

    Player friendPlayer = new Player();
    friendPlayer.setId(3);

    when(playerService.findByUsernamePlayer(anyString())).thenReturn(Optional.of(friendPlayer));

    when(playerService.areFriends(targetPlayer, friendPlayer)).thenReturn(false);

    mockMvc.perform(put(BASE_URL + "/friends/2/friendUsername")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void shouldAddFriendToUser_WithInvalidAdminUser() throws Exception {
    Player nonAdminPlayer = new Player();
    nonAdminPlayer.setId(1);
    nonAdminPlayer.setIs_admin(false);

    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(nonAdminPlayer));

    mockMvc.perform(put(BASE_URL + "/friends/2/friendUsername")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "admin", password = "dobble_admin")
  void shouldAddFriendToUser_TargetUserNotFound() throws Exception {
    when(playerService.findPlayer(anyInt())).thenReturn(Optional.empty());

    mockMvc.perform(put(BASE_URL + "/friends/1/friend_username")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(username = "admin", password = "dobble_admin")
  void shouldAddFriendToUser_FriendNotFound() throws Exception {
    when(playerService.findPlayer(anyInt())).thenReturn(Optional.of(new Player()));
    when(playerService.findByUsernamePlayer(anyString())).thenReturn(Optional.empty());

    mockMvc.perform(put(BASE_URL + "/friends/1/friend_username")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  // Test getFriendsOfUser
  @Test
  @WithMockUser(username = "admin", password = "dobble_admin")
  void shouldGetFriendsOfUser() throws Exception {
    Player adminPlayer = new Player();
    adminPlayer.setId(1);
    adminPlayer.setIs_admin(true);

    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(adminPlayer));

    Player targetPlayer = new Player();
    targetPlayer.setId(2);
    targetPlayer.setIs_admin(false);

    List<Player> friendsList = new ArrayList<>();
    Player friend1 = new Player();
    friend1.setId(3);
    friendsList.add(friend1);

    Player friend2 = new Player();
    friend2.setId(4);
    friendsList.add(friend2);

    targetPlayer.setFriends(friendsList);

    when(playerService.findPlayer(anyInt())).thenReturn(Optional.of(targetPlayer));

    mockMvc.perform(get(BASE_URL + "/friends/2")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(2))
        .andExpect(jsonPath("$.[0].id").value(3))
        .andExpect(jsonPath("$.[1].id").value(4));
  }

  @Test
  @WithMockUser(username = "admin", password = "dobble_admin")
  void shouldGetFriendsOfUser_WithInvalidUser() throws Exception {
    Player nonAdminPlayer = new Player();
    nonAdminPlayer.setId(1);
    nonAdminPlayer.setIs_admin(false);

    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(nonAdminPlayer));

    Player targetPlayer = new Player();
    targetPlayer.setId(2);
    targetPlayer.setIs_admin(false);

    when(playerService.findPlayer(anyInt())).thenReturn(Optional.of(targetPlayer));

    mockMvc.perform(get(BASE_URL + "/friends/2")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "admin", password = "dobble_admin")
  void shouldGetFriendsOfUser_WithInvalidTargetUser() throws Exception {
    Player adminPlayer = new Player();
    adminPlayer.setId(1);
    adminPlayer.setIs_admin(true);

    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(adminPlayer));

    when(playerService.findPlayer(anyInt())).thenReturn(Optional.empty());

    mockMvc.perform(get(BASE_URL + "/friends/100")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  // Test getFriendsOfMe
  @Test
  void shouldGetFriendsOfMe_WhenUserIsAuthenticated() {
    Player currentUser = new Player();
    currentUser.setId(1);
    currentUser.setUsername("currentUser");
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(currentUser));

    Player friend1 = new Player();
    friend1.setId(2);
    friend1.setUsername("friend1");
    Player friend2 = new Player();
    friend2.setId(3);
    friend2.setUsername("friend2");
    currentUser.setFriends(List.of(friend1, friend2));

    ResponseEntity<?> responseEntity = playerController.getFriendsOfMe();

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    List<PublicPlayerDto> friendsList = (List<PublicPlayerDto>) responseEntity.getBody();
    assertNotNull(friendsList);
    assertEquals(2, friendsList.size());
  }

  @Test
  void shouldGetFriendsOfMe_WhenUserIsNotAuthenticated() {
    when(playerService.findCurrentPlayer()).thenReturn(Optional.empty());

    ResponseEntity<?> responseEntity = playerController.getFriendsOfMe();

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  // Test deleteFriendToUser
  @Test
  void shouldDeleteFriendToUser() {
    Player adminUser = new Player();
    adminUser.setIs_admin(true);
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(adminUser));

    Player targetUser = new Player();
    targetUser.setId(1);
    Player friendUser = new Player();
    friendUser.setUsername("friend_username");

    when(playerService.findPlayer(1)).thenReturn(Optional.of(targetUser));
    when(playerService.findByUsernamePlayer("friend_username")).thenReturn(Optional.of(friendUser));
    when(playerService.areFriends(targetUser, friendUser)).thenReturn(true);

    ResponseEntity<?> responseEntity = playerController.deleteFriendToUser(1, "friend_username");

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    verify(playerService, times(1)).removeFriend(targetUser, friendUser);
  }

  @Test
  void shouldDeleteFriendToUser_WhenTargetOrFriendUserNotFound() {
    Player adminUser = new Player();
    adminUser.setIs_admin(true);
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(adminUser));

    when(playerService.findPlayer(1)).thenReturn(Optional.empty());

    ResponseEntity<?> responseEntity = playerController.deleteFriendToUser(1, "friend_username");

    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    verify(playerService, never()).removeFriend(any(), any());
  }

  @Test
  void shouldDeleteFriendToUser_WhenUsersAreNotFriends() {
    Player adminUser = new Player();
    adminUser.setIs_admin(true);
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(adminUser));

    Player targetUser = new Player();
    targetUser.setId(1);
    Player friendUser = new Player();
    friendUser.setUsername("friend_username");

    when(playerService.findPlayer(1)).thenReturn(Optional.of(targetUser));
    when(playerService.findByUsernamePlayer("friend_username")).thenReturn(Optional.of(friendUser));
    when(playerService.areFriends(targetUser, friendUser)).thenReturn(false);

    ResponseEntity<?> responseEntity = playerController.deleteFriendToUser(1, "friend_username");

    assertEquals(HttpStatus.NOT_MODIFIED, responseEntity.getStatusCode());
    verify(playerService, never()).removeFriend(any(), any());
  }

  // Test addFriendToMe
  @Test
  void shouldAddFriendToMe_WhenFriendAddedSuccessfully() {
    Player target = new Player();
    target.setId(1);
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(target));

    Player friend = new Player();
    friend.setId(2);
    when(playerService.findByUsernamePlayer("friend_username")).thenReturn(Optional.of(friend));

    when(playerService.areFriends(target, friend)).thenReturn(false);

    ResponseEntity<?> responseEntity = playerController.addFriendToMe("friend_username");

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    verify(playerService, times(1)).addFriend(target, friend);
  }

  @Test
  void shouldAddFriendToMe_WhenEitherUserNotFound() {
    Player target = new Player();
    target.setId(1);
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(target));

    when(playerService.findByUsernamePlayer("friend_username")).thenReturn(Optional.empty());

    ResponseEntity<?> responseEntity = playerController.addFriendToMe("friend_username");

    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    verify(playerService, never()).addFriend(any(), any());
  }

  @Test
  void shouldAddFriendToMe_WhenUsersAreAlreadyFriends() {
    Player target = new Player();
    target.setId(1);
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(target));

    Player friend = new Player();
    friend.setId(2);
    when(playerService.findByUsernamePlayer("friend_username")).thenReturn(Optional.of(friend));

    when(playerService.areFriends(target, friend)).thenReturn(true);

    ResponseEntity<?> responseEntity = playerController.addFriendToMe("friend_username");

    assertEquals(HttpStatus.NOT_MODIFIED, responseEntity.getStatusCode());
    verify(playerService, never()).addFriend(any(), any());
  }

  // Test deleteFriendToMe
  @Test
  void shouldDeleteFriendToMe_WhenEitherUserNotFound() {
    Player target = new Player();
    target.setId(1);
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(target));

    when(playerService.findByUsernamePlayer("friend_username")).thenReturn(Optional.empty());

    ResponseEntity<?> responseEntity = playerController.deleteFriendToMe("friend_username");

    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    verify(playerService, never()).removeFriend(any(), any());
  }

  @Test
  void shouldDeleteFriendToMe_WhenUsersAreNotFriends() {
    Player target = new Player();
    target.setId(1);
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(target));

    Player friend = new Player();
    friend.setId(2);
    when(playerService.findByUsernamePlayer("friend_username")).thenReturn(Optional.of(friend));

    when(playerService.areFriends(target, friend)).thenReturn(false);

    ResponseEntity<?> responseEntity = playerController.deleteFriendToMe("friend_username");

    assertEquals(HttpStatus.NOT_MODIFIED, responseEntity.getStatusCode());
    verify(playerService, never()).removeFriend(any(), any());
  }

  @Test
  void shouldDeleteFriendToMe_WhenFriendRemovedSuccessfully() {
    Player target = new Player();
    target.setId(1);
    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(target));

    Player friend = new Player();
    friend.setId(2);
    when(playerService.findByUsernamePlayer("friend_username")).thenReturn(Optional.of(friend));

    when(playerService.areFriends(target, friend)).thenReturn(true);

    ResponseEntity<?> responseEntity = playerController.deleteFriendToMe("friend_username");

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    verify(playerService, times(1)).removeFriend(target, friend);
  }

  // Test getAll
  @Test
  @WithMockUser(username = "admin", password = "dobble_admin")
  void shouldGetAll() throws Exception {
    Player adminPlayer = new Player();
    adminPlayer.setId(1);
    adminPlayer.setIs_admin(true);
    adminPlayer.setUsername("admin");
    adminPlayer.setEmail("admin@example.com");

    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(adminPlayer));

    List<Player> playerList = new ArrayList<>();

    Player player1 = new Player();
    player1.setId(2);
    player1.setUsername("player1");
    player1.setEmail("player1@example.com");

    Player player2 = new Player();
    player2.setId(3);
    player2.setUsername("player2");
    player2.setEmail("player2@example.com");

    playerList.add(player1);
    playerList.add(player2);

    when(playerService.findAll()).thenReturn(Optional.of(playerList));

    mockMvc.perform(get(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].id").exists())
        .andExpect(jsonPath("$[0].username").value("player1"))
        .andExpect(jsonPath("$[0].email").value("player1@example.com"))
        .andExpect(jsonPath("$[0].is_admin").value(false))

        .andExpect(jsonPath("$[1].id").exists())
        .andExpect(jsonPath("$[1].username").value("player2"))
        .andExpect(jsonPath("$[1].email").value("player2@example.com"))
        .andExpect(jsonPath("$[1].is_admin").value(false));
  }

  @Test
  @WithMockUser(username = "dobble", password = "dobble")
  void testGetAllAsNonAdmin() throws Exception {
    Player nonAdminPlayer = new Player();
    nonAdminPlayer.setId(2);
    nonAdminPlayer.setIs_admin(false);

    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(nonAdminPlayer));

    mockMvc.perform(get(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "admin", password = "dobble_admin")
  void testGetAllWithNoContent() throws Exception {
    Player adminPlayer = new Player();
    adminPlayer.setId(1);
    adminPlayer.setIs_admin(true);

    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(adminPlayer));

    List<Player> emptyPlayerList = new ArrayList<>();

    when(playerService.findAll()).thenReturn(Optional.of(emptyPlayerList));

    mockMvc.perform(get(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  // Test getById
  @Test
  @WithMockUser(username = "admin", password = "dobble_admin")
  void shouldGetById() throws Exception {
    Player adminPlayer = new Player();
    adminPlayer.setId(1);
    adminPlayer.setIs_admin(true);

    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(adminPlayer));

    Player player = new Player();
    player.setId(2);
    player.setUsername("testuser");

    when(playerService.findPlayer(anyInt())).thenReturn(Optional.of(player));

    mockMvc.perform(get("/api/player/2")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(2))
        .andExpect(jsonPath("$.username").value("testuser"));
  }

  @Test
  @WithMockUser(username = "admin", password = "dobble_admin")
  void shouldGetById_WithInvalidId() throws Exception {
    Player adminPlayer = new Player();
    adminPlayer.setId(1);
    adminPlayer.setIs_admin(true);

    when(playerService.findCurrentPlayer()).thenReturn(Optional.of(adminPlayer));

    when(playerService.findPlayer(anyInt())).thenReturn(Optional.empty());

    mockMvc.perform(get(BASE_URL + "/100")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }
}
