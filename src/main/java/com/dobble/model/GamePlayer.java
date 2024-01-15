package com.dobble.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.dobble.dto.GamePlayerDto;
import com.dobble.model.base.BaseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
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
  @JsonIgnore
  private Player player;

  // Importante el orden de las cartas para que se mantenga la aleatoriedad
  @NotNull
  @OneToMany(mappedBy = "game_player", orphanRemoval = true)
  @OrderColumn(name = "card_insertion_order")
  private List<Card> cards = new ArrayList<Card>();

  @NotNull
  private Integer strikes = 0;

  @Transient
  public GamePlayerDto getAsDto() {
    return new GamePlayerDto(this);
  }

  @Transient
  @JsonProperty("card")
  public Optional<Card> getCurrentCard() {
    return this.getCards()
        .stream()
        .filter(c -> c.getRelease_time() == null)
        .map(Optional::ofNullable)
        .findFirst()
        .flatMap(Function.identity());
  }

  @Transient
  @JsonIgnore
  public int getNumberOfAvailableCards() {
    return (int) this.getCards().stream()
        .filter(c -> c.getRelease_time() == null)
        .count();
  }

  @Transient
  @JsonIgnore
  public Integer getPlayerId() {
    return this.getPlayer().getId();
  }
}
