package org.springframework.samples.petclinic.dto;

import java.util.Optional;

import org.springframework.samples.petclinic.model.Card;
import org.springframework.samples.petclinic.model.GamePlayer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GamePlayerDto extends PublicPlayerDto {
  private Optional<Card> current_card;

  public GamePlayerDto(GamePlayer gp) {
    super(gp.getPlayer());
    this.current_card = gp.getCurrentCard();
  }
}
