package org.springframework.samples.petclinic.model;


import org.springframework.samples.petclinic.model.base.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "game_players")
public class GamePlayer extends BaseEntity{
  public GamePlayer(Player player, Game game) {
    this.game = game;
    this.player= player;
}
  
  @NotNull
  @ManyToOne
  @JoinColumn(name = "game_id")
  Game game; 

  @NotNull
  @ManyToOne
  @JoinColumn(name= "player_id")
  Player player;

}
