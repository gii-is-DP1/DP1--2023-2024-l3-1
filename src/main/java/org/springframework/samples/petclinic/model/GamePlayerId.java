package org.springframework.samples.petclinic.model;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class GamePlayerId implements Serializable{
  private String game;
  private Integer player;

  public GamePlayerId() {

  }
  public GamePlayerId(Game game,Player player) {
    this.game = game.getName();
    this.player = player.getId(); 
  }

}
