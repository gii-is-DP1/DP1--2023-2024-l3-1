package org.springframework.samples.petclinic.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Game;
import org.springframework.samples.petclinic.model.GamePlayer;
import org.springframework.samples.petclinic.model.HitoPartida;
import org.springframework.samples.petclinic.repositories.HitoPartidaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HitoPartidaService {

  private final HitoPartidaRepository hitoPartidaRepository;

  @Autowired
  public HitoPartidaService(HitoPartidaRepository hitoPartidaRepository) {
    this.hitoPartidaRepository = hitoPartidaRepository;
  }

  @Transactional(readOnly = true)
  public List<HitoPartida> findAll() {
    return hitoPartidaRepository.findAll();
  }

  @Transactional
  public void finalizarPartidaSiNecesario(Game game) {
    List<GamePlayer> gamePlayers = game.getAllGamePlayers();
    GamePlayer ganador = null;
    boolean partidaFinalizada = false;

    for (GamePlayer gamePlayer : gamePlayers) {
      if (gamePlayer.getCards().isEmpty()) {
        ganador = gamePlayer;
        partidaFinalizada = true;
        break;
      }
    }

    if (partidaFinalizada) {
      gamePlayers.sort(Comparator.comparingInt(gp -> gp.getCards().size()));

      int posicion = 1;
      int cartasAnteriores = gamePlayers.get(0).getCards().size();

      for (GamePlayer gamePlayer : gamePlayers) {
        int cartasActuales = gamePlayer.getCards().size();
        if (cartasActuales < cartasAnteriores) {
          cartasAnteriores = cartasActuales;
          posicion++;
        }
        HitoPartida hito = new HitoPartida();
        hito.setRank(posicion);
        hito.setGamePlayer(gamePlayer);
        hito.setGame(game);
        hitoPartidaRepository.save(hito);
      }
    }
  }

  @Transactional(readOnly = true)
  public long calcularTiempoTotalPartida(Game game) {
    LocalDateTime inicio = game.getStart();
    LocalDateTime fin = game.getFinish();

    if (inicio != null && fin != null) {
      return java.time.Duration.between(inicio, fin).getSeconds();
    } else {
      return 0;
    }
  }

  @Transactional
  public void registrarTiempoRespuesta(GamePlayer gamePlayer, double tiempoDeRespuesta) {
    Game game = gamePlayer.getGame();
    List<HitoPartida> hitosPartida = List.of(hitoPartidaRepository.findByGame(game).get());

    for (HitoPartida hitoPartida : hitosPartida) {
      if (hitoPartida.getGamePlayer().equals(gamePlayer)) {
        List<Double> tiemposRespuesta = hitoPartida.getTiemposRespuesta();
        if (tiemposRespuesta == null) {
          tiemposRespuesta = new ArrayList<>();
        }
        tiemposRespuesta.add(tiempoDeRespuesta);
        hitoPartida.setTiemposRespuesta(tiemposRespuesta);
        hitoPartidaRepository.save(hitoPartida);
        break;
      }
    }
  }
}
