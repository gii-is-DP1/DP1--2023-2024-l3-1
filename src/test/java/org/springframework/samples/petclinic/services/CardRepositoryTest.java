package org.springframework.samples.petclinic.services;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Card;
import org.springframework.samples.petclinic.repositories.CardRepository;

@SpringBootTest
@AutoConfigureTestDatabase
public class CardRepositoryTest {

  @Autowired
  private CardRepository cardRepository;

  @Test
  void testRepositoryFindAll() {
    List<Card> cards = cardRepository.findAll();
    assertNotNull(cards);
  }
}
