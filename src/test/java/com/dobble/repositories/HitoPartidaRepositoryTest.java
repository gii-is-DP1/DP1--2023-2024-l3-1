package com.dobble.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.dobble.model.Game;
import com.dobble.model.HitoPartida;
import com.dobble.repositories.GameRepository;
import com.dobble.repositories.HitoPartidaRepository;

@DataJpaTest
public class HitoPartidaRepositoryTest {

  @Autowired
  HitoPartidaRepository hpr;

  @Autowired
  GameRepository gr;

  @Test
  void testRepositoryFindAll() {
    List<HitoPartida> hitosPartida = hpr.findAll();
    assertNotNull(hitosPartida);
  }

  @Test
  void testFindByGame() {
    Optional<Game> g = gr.findByName("partida2");
    // Crear un HitoPartida de prueba asociado al Game
    HitoPartida hitoPartida = new HitoPartida();
    hitoPartida.setRank(1);
    // hitoPartida.setTiemposRespuesta(List.of(1.0, 2.0, 3.0));
    // hitoPartida.setTiempoTotalPartida(10);
    hitoPartida.setGame(g.get());
    // Guardar el HitoPartida en la base de datos
    hpr.save(hitoPartida);
    Optional<HitoPartida> h = hpr.findByGame(g.get());
    assertNotNull(h);
    assertEquals("partida2", h.get().getGame().getName());
  }
}
