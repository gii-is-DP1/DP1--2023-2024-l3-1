package com.dobble.repositories;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import com.dobble.model.Figure;
import com.dobble.repositories.FigureRepository;

@SpringBootTest
@AutoConfigureTestDatabase
public class FigureRepositoryTest {

  @Autowired
  private FigureRepository figureRepository;

  @Test
  public void testFindAll() {
    Optional<List<Figure>> allFigures = Optional.of(figureRepository.findAll());
    assertTrue(allFigures.isPresent());
    assertTrue(allFigures.get().isEmpty());
  }
}
