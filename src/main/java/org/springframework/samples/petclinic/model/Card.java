package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.samples.petclinic.model.base.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cards")
public class Card extends BaseEntity {
  //TODO eliminar seguramente
  /*
   * @Size(min = 8, max = 8)
   * 
   * @ElementCollection
   * 
   * @Enumerated(EnumType.STRING)
   * 
   * @CollectionTable(name = "card_icons", joinColumns = @JoinColumn(name =
   * "card_id"))
   * 
   * @Column(name = "icon")
   * private List<Icon> icons;
   */

  @Size(min = 8, max = 8)
  @ManyToMany(fetch = FetchType.EAGER)
  private List<Figure> figures = new ArrayList<>();

  public Boolean hasIcon(Figure figure) {
    return this.figures.contains(figure);
  }

  public Figure getMatchingIcons(Card card) {
    return this.figures.stream()
        .filter(figure -> card.getFigures().contains(figure))
        .findFirst()
        .get();
  }

}
