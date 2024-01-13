package org.springframework.samples.petclinic.model;

import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.samples.petclinic.model.base.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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

  @NotNull
  private List<Double> tiemposRespuesta;

  @NotNull
  private long tiempoTotalPartida;

  @OneToOne
  @JoinColumn(name = "game_player_id")
  private GamePlayer gamePlayer;

  @ManyToOne
  @JoinColumn(name = "game_id")
  private Game game;
}
