package com.dobble;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import com.dobble.model.Card;
import com.dobble.repositories.CardRepository;

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
