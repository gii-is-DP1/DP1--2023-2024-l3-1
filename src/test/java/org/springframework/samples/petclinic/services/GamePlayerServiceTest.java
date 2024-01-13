package org.springframework.samples.petclinic.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Card;
import org.springframework.samples.petclinic.model.Game;
import org.springframework.samples.petclinic.model.GamePlayer;
import org.springframework.samples.petclinic.model.Hand;
import org.springframework.samples.petclinic.model.Player;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase
public class GamePlayerServiceTest {

  @Autowired
  private GamePlayerService gamePlayerService;

  @Autowired
  private GameService gameService;

  @Autowired
  private PlayerService playerService;

  @Autowired
  private HandService handService;

  @Autowired
  private CardService cardService;

  @Test
  @Transactional
  public void testSaveGamePlayer() {
    // Contamos los GamePlayers iniciales
    //Iterable<GamePlayer> iterableGamePlayers = this.gamePlayerService.findAll();
    int count = 0;
    // for (GamePlayer gamePlayer : iterableGamePlayers) {
    //   count++;
    // }

    // Inicializamos el nuevo GamePlayer
    GamePlayer newGamePlayer = new GamePlayer();
    // Obtenemos las propiedades necesarias
    Optional<Game> game = gameService.findByNameGame("partida2");
    Optional<Player> player = playerService.findPlayer(1);

    // Asi creamos Hand
    Hand newHand = new Hand();
    List<Card> cards = this.cardService.findAll().get();
    List<Card> listCards = cards.subList(0, 3);
    newHand.setCards(listCards);
    handService.saveHand(newHand);

    // Establecemos las propiedades obtenidas antes en el nuevo GamePlayer
    newGamePlayer.setGame(game.get());
    newGamePlayer.setPlayer(player.get());
    newGamePlayer.setHand(newHand);

    // Guardamos el nuevo GamePlayer
    gamePlayerService.save(newGamePlayer);

    // Volvemos a contar los GamePlayers
    // Iterable<GamePlayer> iterableGamePlayersWithNewGamePlayer = this.gamePlayerService.findAll();
    int finalCount = 0;
    // for (GamePlayer gamePlayer : iterableGamePlayersWithNewGamePlayer) {
    //   finalCount++;
    // }

    // Deberia de haber un elemento más que al principio
    assertEquals(count + 1, finalCount);
  }
}
