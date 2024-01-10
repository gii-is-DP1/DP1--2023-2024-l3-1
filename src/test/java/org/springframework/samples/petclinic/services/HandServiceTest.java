package org.springframework.samples.petclinic.services;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Card;
import org.springframework.samples.petclinic.model.Hand;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase
public class HandServiceTest {

  @Autowired
  private HandService handService;

  @Autowired
  private CardService cardService;

  @Test
  @Transactional
  public void testServiceSaveHand() {
    Iterable<Hand> iterableHands = this.handService.findAll();
    int count = 0;
    for (Hand hand : iterableHands) {
      count++;
    }

    Hand newHand = new Hand();
    List<Card> cards = this.cardService.findAll().get();
    List<Card> listCards = cards.subList(0, 3);
    newHand.setCards(listCards);

    handService.saveHand(newHand);

    Iterable<Hand> iterableHandsWithNewHand = this.handService.findAll();
    int finalCount = 0;
    for (Hand hand : iterableHandsWithNewHand) {
      finalCount++;
    }

    assertEquals(count + 1, finalCount);

  }

  @Test
  public void testServiceFindAll() {
    Iterable<Hand> iterableHands = handService.findAll();
    List<Hand> hands = new ArrayList<>();
    Iterator<Hand> iterator = iterableHands.iterator();
    while (iterator.hasNext()) {
      Hand hand = iterator.next();
      hands.add(hand);
    }
    assertNotNull(hands);
  }
}
