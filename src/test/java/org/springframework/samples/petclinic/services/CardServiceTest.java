package org.springframework.samples.petclinic.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Card;
import org.springframework.samples.petclinic.repositories.CardRepository;

@SpringBootTest
@AutoConfigureTestDatabase
public class CardServiceTest {
  @Mock
  private CardRepository cardRepository;

  @Mock
  private FigureService figureService;

  @InjectMocks
  private CardService cardService;

  @Test
  void testFindAllWithData() {
    List<Card> mockCards = Arrays.asList(new Card(), new Card());
    when(cardRepository.findAll()).thenReturn(mockCards);

    Optional<List<Card>> result = cardService.findAll();

    assertEquals(Optional.of(mockCards), result);
  }

  @Test
  void testFindAllWhenNoData() {
    when(cardRepository.findAll()).thenReturn(Collections.emptyList());

    Optional<List<Card>> result = cardService.findAll();

    assertEquals(List.of(), result.get());
  }

  @Test
  void testSaveCard() {
    Card cardToSave = new Card();
    when(cardRepository.save(cardToSave)).thenReturn(cardToSave);

    Card savedCard = cardService.saveCard(cardToSave);

    verify(cardRepository).save(cardToSave);
    assertEquals(cardToSave, savedCard);
  }

  @Test
  void testDeleteAll() {
    List<Card> cardsToDelete = Arrays.asList(new Card(), new Card());

    cardService.deleteAll(cardsToDelete);

    verify(cardRepository).deleteAll(cardsToDelete);
  }

  @Test
  void testCreateCards() {
    when(figureService.createFigure(any(), anyInt())).thenReturn(null);

    List<Card> createdCards = cardService.createCards();

    verify(figureService, times(createdCards.size() * 8)).createFigure(any(), anyInt());

    assertNotEquals(0, createdCards.size(), "Created cards list should not be empty.");
    assertEquals(createdCards.size() * 8, createdCards.stream().map(Card::getFigures).mapToInt(List::size).sum(),
        "Total number of figures in all cards should be 8 times the number of created cards.");
  }

}
