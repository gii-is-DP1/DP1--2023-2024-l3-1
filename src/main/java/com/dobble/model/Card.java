package com.dobble.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.dobble.model.base.HiddenBaseEntity;
import com.dobble.model.enums.Icon;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cards")
public class Card extends HiddenBaseEntity {
  @OneToMany(mappedBy = "card")
  @OrderColumn(name = "figures_insertion_order")
  private List<Figure> figures = new ArrayList<Figure>();

  @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
  @JsonIgnore
  private LocalDateTime release_time;

  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "game_player_id")
  private GamePlayer game_player;

  @OneToOne(optional = true)
  @JsonIgnore
  @JoinColumn(name = "game_id")
  private Game game;

  @JsonIgnore
  @Transient
  public Boolean hasIcon(Icon icon) {
    return this.figures.stream().anyMatch(f -> f.getIcon() == icon);
  }
}
