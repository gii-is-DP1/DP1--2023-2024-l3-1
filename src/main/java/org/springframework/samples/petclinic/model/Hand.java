package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.samples.petclinic.model.base.BaseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
  @OneToMany(mappedBy = "hand", fetch = FetchType.EAGER, orphanRemoval = true)
  @Fetch(FetchMode.SELECT)
  private List<Card> cards = new ArrayList<Card>();

  @NotNull
  private Integer strikes = 0;

  @Transient
  @JsonIgnore
  public Optional<Card> getCurrentCard() {
    return this.getCards()
      .stream()
      .filter(c -> c.getRelease_time() == null)
      .map(Optional::ofNullable)
      .findFirst()
      .flatMap(Function.identity());
  }
}
