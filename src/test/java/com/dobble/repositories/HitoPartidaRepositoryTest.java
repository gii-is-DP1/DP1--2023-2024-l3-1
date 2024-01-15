package com.dobble.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.dobble.model.Game;
import com.dobble.model.HitoPartida;
import com.dobble.repositories.GameRepository;
import com.dobble.repositories.HitoPartidaRepository;

@DataJpaTest
@ExtendWith(MockitoExtension.class)

public class HitoPartidaRepositoryTest {

  @Mock
  HitoPartidaRepository hpr;

  @Mock
  GameRepository gr;

  @Test
  void testRepositoryFindAll() {
    List<HitoPartida> hitosPartida = hpr.findAll();
    assertNotNull(hitosPartida);
  }

  @Test
  public void testFindByGame() {
    Game game = new Game();
    HitoPartida expectedHitoPartida = new HitoPartida();

    when(hpr.findByGame(any(Game.class)))
        .thenReturn(Optional.of(expectedHitoPartida));

    Optional<HitoPartida> actualHitoPartida = hpr.findByGame(game);

    assertEquals(Optional.of(expectedHitoPartida), actualHitoPartida);
  }
}
