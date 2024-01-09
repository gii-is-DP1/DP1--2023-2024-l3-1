package org.springframework.samples.petclinic.model;

import java.util.List;

import org.springframework.samples.petclinic.model.base.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Hand extends BaseEntity {
  
  @NotNull
  @OneToMany
  private List<Card> cards;

  @Transient
  public Card getCurrentCard() {
    if (cards != null && !cards.isEmpty()) {
      return cards.get(0);
    } else {
      return null;
    }
  }

  @Transient
  public Card getNextCard() {
    if (cards != null && !cards.isEmpty()) {
      cards.remove(0);
      return cards.get(0);
    } else {
      return null;
    }
  }

  @Transient
  public boolean isLastCard(){
    return !cards.isEmpty() && cards != null && cards.size() == 1;
  }

}
