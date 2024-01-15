package com.dobble.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.dobble.model.base.BaseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "hito_partidas")
public class HitoPartida extends BaseEntity {
  @Min(1)
  @Max(8)
  @NotNull
  Integer rank;

  @OneToOne
  @JoinColumn(name = "game_player_id")
  private GamePlayer gamePlayer;

  @ManyToOne
  @JoinColumn(name = "game_id")
  private Game game;

  @Transient
  @JsonIgnore
  public List<Long> getTiemposRespuesta() {
    List<Long> duraciones = new ArrayList<Long>();
    List<LocalDateTime> fechas = this.getGamePlayer()
        .getCards()
        .stream()
        .map(c -> c.getRelease_time())
        .collect(Collectors.toList());

    for (Integer i = 1; i < fechas.size() - 1; i++) {
      duraciones.add(Duration.between(fechas.get(i - 1), fechas.get(i)).toNanos());
    }

    return duraciones;
  }

  @Transient
  @JsonIgnore
  public Long getTiempoTotalPartida() {
    return this.getTiemposRespuesta()
        .stream()
        .mapToLong(Long::longValue)
        .sum();
  }
}
