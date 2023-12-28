package org.springframework.samples.petclinic.model;

import java.util.List;

import org.springframework.samples.petclinic.model.base.BaseEntity;
import org.springframework.samples.petclinic.model.enums.Icon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cards")
public class Card extends BaseEntity{

  @Size(min = 8, max = 8)
  @Enumerated(EnumType.STRING)
  @Column(name = "icons")
  private List<Icon> icons;

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
