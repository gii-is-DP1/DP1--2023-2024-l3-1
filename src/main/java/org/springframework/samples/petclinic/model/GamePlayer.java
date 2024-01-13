package org.springframework.samples.petclinic.model;

import java.util.Optional;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.samples.petclinic.dto.GamePlayerDto;
import org.springframework.samples.petclinic.model.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
  @ManyToOne(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  @JoinColumn(name = "game_id")
  @JsonIgnore
  private Game game;

  @NotNull
  @ManyToOne(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  @JoinColumn(name = "player_id")
  @JsonIgnore
  private Player player;

  @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
  @Fetch(FetchMode.SELECT)
  @JsonIgnore
  @JoinColumn(name = "hand_id")
  private Hand hand; 

  @Transient
  public GamePlayerDto getAsDto() {
    return new GamePlayerDto(this);
  }

  @Transient
  @JsonProperty("card")
  public Optional<Card> getCurrentCard() {
    return this.getHand() != null ? this.getHand().getCurrentCard() : Optional.empty();
  }

  @Transient
  @JsonIgnore
  public Integer getPlayerId(){
    return this.getPlayer().getId(); 
  }
}
