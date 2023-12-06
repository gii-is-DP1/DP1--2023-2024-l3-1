package org.springframework.samples.petclinic.model;

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
public class GamePlayer{
  
  @Id
  @NotNull
  @ManyToOne
  @JoinColumn(name = "game_id", columnDefinition = "VARCHAR(255)")
  private Game game; 

  @Id
  @NotNull
  @ManyToOne
  @JoinColumn(name= "player_id")
  private Player player;

}
