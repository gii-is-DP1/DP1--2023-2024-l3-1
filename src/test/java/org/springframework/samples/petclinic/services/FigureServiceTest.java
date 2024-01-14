package org.springframework.samples.petclinic.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.samples.petclinic.model.Figure;
import org.springframework.samples.petclinic.model.enums.Icon;
import org.springframework.samples.petclinic.repositories.FigureRepository;

@SpringBootTest
@AutoConfigureTestDatabase
public class FigureServiceTest {

  @Mock
  private FigureRepository figureRepository;

  @InjectMocks
  private FigureService figureService;

  @Test
  void testFindAll() {
    List<Figure> mockFigures = Arrays.asList(new Figure(), new Figure());
    when(figureRepository.findAll()).thenReturn(mockFigures);

    Optional<List<Figure>> result = figureService.findAll();

    assertEquals(Optional.of(mockFigures), result);
  }

  @Test
  void testFindAllWhenNoData() {
    when(figureRepository.findAll()).thenReturn(Collections.emptyList());

    Optional<List<Figure>> result = figureService.findAll();

    assertEquals(List.of(), result.get());
  }

  @Test
  void testCreateFigure() {
    Card card = new Card();
    int iconIndex = 0;

    Figure createdFigure = figureService.createFigure(card, iconIndex);

    verify(figureRepository, times(1)).save(any(Figure.class));

    assertNotNull(createdFigure);
    assertNotNull(createdFigure.getSize());
    assertEquals(Icon.values()[iconIndex], createdFigure.getIcon());
    assertEquals(card, createdFigure.getCard());
  }

  @Test
  void testCreateFigureNegative() {
    Card card = new Card();
    int iconIndex = 0;

    Figure createdFigure = figureService.createFigure(card, iconIndex);

    verify(figureRepository, times(1)).save(any(Figure.class));

    assertNotEquals(0, createdFigure.getRotation());
    assertNotEquals(Icon.values()[1], createdFigure.getIcon());
    assertNotEquals(new Card(), createdFigure.getCard());
  }
}
