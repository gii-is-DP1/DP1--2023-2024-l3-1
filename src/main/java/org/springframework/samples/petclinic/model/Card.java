package org.springframework.samples.petclinic.model;

import java.util.Set;

import org.springframework.samples.petclinic.model.enums.Icon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cards")
public class Card {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Size(min = 8, max = 8)
  @Enumerated(EnumType.STRING)
  @Column(name = "icon")
  private Set<Icon> icons;

  public Boolean hasIcon(Icon icon) {
    return this.icons.contains(icon);
  }

  public Icon getMatchingIcons(Card card) {
    return this.icons.stream()
        .filter(icon -> card.getIcons().contains(icon))
        .findFirst()
        .get();
  }

}
