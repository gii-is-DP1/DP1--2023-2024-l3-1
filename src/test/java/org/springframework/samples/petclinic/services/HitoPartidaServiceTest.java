package org.springframework.samples.petclinic.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Card;
import org.springframework.samples.petclinic.model.Game;
import org.springframework.samples.petclinic.model.GamePlayer;
import org.springframework.samples.petclinic.model.Hand;
import org.springframework.samples.petclinic.model.HitoPartida;
import org.springframework.samples.petclinic.repositories.HitoPartidaRepository;

@SpringBootTest
@AutoConfigureTestDatabase
public class HitoPartidaServiceTest {

  @InjectMocks
  private HitoPartidaService hitoPartidaService;

  @Mock
  private HitoPartidaRepository hitoPartidaRepository;

  @Test
  void testFinalizarPartidaSiNecesario() {
    Game game = new Game();
    List<GamePlayer> gamePlayers = new ArrayList<>();

    GamePlayer winnerPlayer = new GamePlayer();
    Hand winnerHand = new Hand();
    winnerHand.setCards(new ArrayList<>());
    winnerPlayer.setHand(winnerHand);
    gamePlayers.add(winnerPlayer);

    GamePlayer secondPlayer = new GamePlayer();
    Hand secondHand = new Hand();
    List<Card> secondPlayerCards = new ArrayList<>();
    secondPlayerCards.add(new Card());
    secondHand.setCards(secondPlayerCards);
    secondPlayer.setHand(secondHand);
    gamePlayers.add(secondPlayer);

    game.setGame_players(gamePlayers);

    when(hitoPartidaRepository.save(any(HitoPartida.class))).thenReturn(new HitoPartida());

    hitoPartidaService.finalizarPartidaSiNecesario(game);

    verify(hitoPartidaRepository, times(gamePlayers.size())).save(any(HitoPartida.class));
  }

  @Test
  void testCalcularTiempoTotalPartida() {
    HitoPartidaRepository hitoPartidaRepository = mock(HitoPartidaRepository.class);
    HitoPartidaService hitoPartidaService = new HitoPartidaService(hitoPartidaRepository);

    LocalDateTime inicio = LocalDateTime.now().minusHours(1);
    LocalDateTime fin = LocalDateTime.now();

    Game game = new Game();
    game.setStart(inicio);
    game.setFinish(fin);

    long tiempoTotal = hitoPartidaService.calcularTiempoTotalPartida(game);

    assertEquals(java.time.Duration.between(inicio, fin).getSeconds(), tiempoTotal);
  }

  @Test
  void testRegistrarTiempoRespuesta() {
    HitoPartidaRepository hitoPartidaRepository = mock(HitoPartidaRepository.class);
    HitoPartidaService hitoPartidaService = new HitoPartidaService(hitoPartidaRepository);

    Game game = new Game();
    game.setId("1");

    GamePlayer gamePlayer = new GamePlayer();
    gamePlayer.setId(1);
    gamePlayer.setGame(game);

    HitoPartida hitoPartida = new HitoPartida();
    hitoPartida.setId(1);
    hitoPartida.setGame(game);
    hitoPartida.setGamePlayer(gamePlayer);

    when(hitoPartidaRepository.findByGame(game)).thenReturn(Optional.of(hitoPartida));

    double tiempoDeRespuesta = 10.0;

    hitoPartidaService.registrarTiempoRespuesta(gamePlayer, tiempoDeRespuesta);

    verify(hitoPartidaRepository, times(1)).save(any(HitoPartida.class));
  }

}
