package org.springframework.samples.petclinic.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.dto.EditPlayerDto;
import org.springframework.samples.petclinic.dto.SignupRequest;
import org.springframework.samples.petclinic.model.Player;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase
public class PlayerServiceTest {

    @Autowired
    private PlayerService playerService; 

    @Test
    void testFindPlayer() {
        Optional<Player> player= playerService.findPlayer(2);
        assertTrue(player.isPresent());
        assertEquals("dobble",player.get().getUsername());
    }
    @Test
    void testFindPlayerNotFound() {
        Optional<Player> player= playerService.findPlayer(999);
        assertFalse(player.isPresent());
    }
    @Test
    @WithMockUser(username = "dobble", password = "dobble")
    void testFindCurrentPlayer() {
        Optional<Player> player=playerService.findCurrentPlayer();
        assertEquals("dobble",player.get().getUsername());
    }

    @Test
    @WithMockUser(username = "dobble", password = "dobble")
    void testFindCurrentPlayerNotExists() {
        Optional<Player> player=playerService.findCurrentPlayer();
        assertNotEquals("dobbld",player.get().getUsername());
    }

    @Test
    void testFindByUsernamePlayer() {
        Optional<Player> player=playerService.findByUsernamePlayer("dobble");
        assertTrue(player.isPresent());
        assertEquals("dobble",player.get().getUsername());
    }

    @Test
    void testFindByUsernameNotExistPlayer() {
        Optional<Player> player=playerService.findByUsernamePlayer("dobbleNotExists");
        assertFalse(player.isPresent());
    }

    @Test
    void testFindByUsernameNullPlayer() {
        Optional<Player> player=playerService.findByUsernamePlayer(null);
        assertFalse(player.isPresent());
    }
    
     @Test
    void testFindAllNotAdmin() {
        Optional<List<Player>> players= playerService.findAll();
        assertTrue(players.isPresent());
        assertEquals(2, players.get().size());
    }

    @Test
    @Transactional
    void testCreateUser() {
        SignupRequest signupRequest= new SignupRequest(); 
        signupRequest.setUsername("dobbleNew");
        signupRequest.setEmail("dobbleNew@example.com");
        signupRequest.setPassword("dobbleNew");

        Optional<Player> dobbleNewPlayer =playerService.findByUsernamePlayer("dobbleNew") ;
        assertFalse(dobbleNewPlayer.isPresent());

        playerService.createUser(signupRequest);
        Optional<Player> newPlayer =playerService.findByUsernamePlayer("dobbleNew") ;
        assertTrue(newPlayer.isPresent());

    }

    @Test
    @Transactional
    void testUpdatePlayer() {
        Optional<Player> playerToUpdate= playerService.findPlayer(2);
        assertTrue(playerToUpdate.isPresent());
        EditPlayerDto newPlayer = new EditPlayerDto(); 
        newPlayer.setUsername("dobbleUpdate");
        newPlayer.setEmail("dobbleUpdate@example.com");
       

        playerService.updatePlayer(newPlayer, playerToUpdate.get().getId());
        Optional<Player> updatedPlayer = playerService.findPlayer(2);

        assertEquals("dobbleUpdate",updatedPlayer.get().getUsername());
        assertEquals("dobbleUpdate@example.com", updatedPlayer.get().getEmail());
    }


    //Prueba para cuando son amigos y haces addFriend()
    @Test
    @Transactional
    void testAddFriend() {
        Optional<Player> player1= playerService.findPlayer(2); 
        Optional<Player> player2= playerService.findPlayer(3); 

        assertTrue(player1.isPresent() && player2.isPresent());
        assertFalse(player1.get().getFriends().contains(player2.get())); 

        playerService.addFriend(player1.get(), player2.get());

        assertTrue(player1.get().getFriends().contains(player2.get()));
    }



   
    @Test
    @Transactional
    void testRemoveFriend() {
        //We add the player2 to the friends (same as testAddFriend)
        Optional<Player> player1= playerService.findPlayer(2); 
        Optional<Player> player2= playerService.findPlayer(3); 

        assertTrue(player1.isPresent() && player2.isPresent());
        assertFalse(player1.get().getFriends().contains(player2.get())); 

        playerService.addFriend(player1.get(), player2.get());

        assertTrue(player1.get().getFriends().contains(player2.get()));

        //We remove again player2 of friends
        playerService.removeFriend(player1.get(), player2.get());
        assertFalse(player1.get().getFriends().contains(player2.get())); 

    }

    @Test
    @Transactional
    void testAreFriends() {
        Optional<Player> player1= playerService.findPlayer(2); 
        Optional<Player> player2= playerService.findPlayer(3); 

        assertTrue(player1.isPresent() && player2.isPresent());
        assertFalse(playerService.areFriends(player1.get(), player2.get())); 

        playerService.addFriend(player1.get(), player2.get());

        assertTrue(playerService.areFriends(player1.get(), player2.get()));
    }

    @Test
    @Transactional
    void testDeletePlayer() {
        Optional<Player> playerToDelete= playerService.findPlayer(2);
        
        assertTrue(playerToDelete.isPresent());

        playerService.deletePlayer(playerToDelete.get().getId());

        Optional<Player> playerAfterDeletion= playerService.findPlayer(2);
        assertFalse(playerAfterDeletion.isPresent());
    }

    @Test
    void testExistsUser() {
        assertEquals(true, playerService.existsUser("dobble", "dobble@example.com"));
    }

    /* 
    @Test
    void testNotExistsUser() {
        assertEquals(false, playerService.existsUser("dobble", "dobble1000@example.com"));
    }
    */
 

 
}
