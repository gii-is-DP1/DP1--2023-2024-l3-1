package com.dobble.services;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import com.dobble.model.Game;
import com.dobble.model.GamePlayer;
import com.dobble.model.HitoPartida;
import com.dobble.repositories.HitoPartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

  


  @Transactional
  public Integer getMyRank(Game game, GamePlayer gamePlayer) {
    List<GamePlayer> gamePlayers = game.getAllGamePlayers();

    Integer rank = gamePlayers.indexOf(gamePlayer) + 1;

    return rank;
  }

  @Transactional
  public void saveMyRank(Game game, GamePlayer gamePlayer) {
    Integer rank = getMyRank(game, gamePlayer);
    HitoPartida hito = new HitoPartida();
    hito.setGamePlayer(gamePlayer);
    hito.setGame(game);
    hito.setRank(rank);

    hitoPartidaRepository.save(hito);

  }


  @Transactional
  public Boolean isWinner(Game game, GamePlayer gamePlayer) {
    Integer rank = getMyRank(game, gamePlayer);
    return rank == 1 ? true : false;
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
        // List<Double> tiemposRespuesta = hitoPartida.getTiemposRespuesta();
        // if (tiemposRespuesta == null) {
        // tiemposRespuesta = new ArrayList<>();
        // }
        // tiemposRespuesta.add(tiempoDeRespuesta);
        // hitoPartida.setTiemposRespuesta(tiemposRespuesta);
        hitoPartidaRepository.save(hitoPartida);
        break;
      }
    }
  }
}
