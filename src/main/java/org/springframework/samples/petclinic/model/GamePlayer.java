package org.springframework.samples.petclinic.model;

import org.springframework.samples.petclinic.dto.PublicPlayerDto;
import org.springframework.samples.petclinic.model.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "game_players")
public class GamePlayer extends BaseEntity {


  @NotNull
  @ManyToOne
  @JoinColumn(name = "game_id")
  @JsonIgnore
  private Game game;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "player_id")
  private Player player;

  @OneToOne
  @JoinColumn(name = "hand_id")
  private Hand hand; 



  @Transient
  @NotNull
  public PublicPlayerDto getPlayer() {
    return new PublicPlayerDto(this.player);
  }

  @Transient
  @NotNull
  public Integer getPlayerId(){
    return this.player.getId(); 
  }

  @Transient
  @NotNull
  @JsonIgnore
  public Player getRealPlayer(){
    return this.player; 
  }

}
