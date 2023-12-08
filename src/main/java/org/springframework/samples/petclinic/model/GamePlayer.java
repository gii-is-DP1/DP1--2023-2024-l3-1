package org.springframework.samples.petclinic.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@IdClass(GamePlayerId.class)
@Table(name = "game_players")
@JsonIgnoreProperties({"game", "player"})
public class GamePlayer{
  
  @Id
  @NotNull
  @ManyToOne
  @JoinColumn(name = "game_id")
  private Game game; 

  @Id
  @NotNull
  @ManyToOne
  @JoinColumn(name= "player_id")
  private Player player;

}
