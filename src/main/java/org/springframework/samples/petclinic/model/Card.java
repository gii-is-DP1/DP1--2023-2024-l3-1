package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.model.base.HiddenBaseEntity;
import org.springframework.samples.petclinic.model.enums.Icon;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
