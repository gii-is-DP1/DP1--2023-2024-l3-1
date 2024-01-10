package org.springframework.samples.petclinic.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Card;

@SpringBootTest
@AutoConfigureTestDatabase
public class CardServiceTest {
  @Autowired
  private CardService cardService;

  @Test
  public void testFindAll() {
    Optional<List<Card>> allGames = cardService.findAll();
    assertTrue(allGames.isPresent());
    assertFalse(allGames.get().isEmpty());
  }
}
