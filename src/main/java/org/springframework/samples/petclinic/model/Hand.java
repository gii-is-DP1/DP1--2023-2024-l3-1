package org.springframework.samples.petclinic.model;

import java.util.List;

import org.springframework.samples.petclinic.model.base.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Hand extends BaseEntity {
  
  @NotNull
  @OneToMany
  @Size(max = 8)
  private List<Card> cards;

  @Transient
  @NotNull
  public Card getCurrentCard() {
    if (cards != null && !cards.isEmpty()) {
      return cards.get(0);
    } else {
      return null;
    }
  }

  @Transient
  @NotNull
  public Card getNextCard() {
    if (cards != null && !cards.isEmpty()) {
      cards.remove(0);
      return cards.get(0);
    } else {
      return null;
    }
  }

}
