package com.dobble.dto;

import java.util.Optional;

import com.dobble.model.Card;
import com.dobble.model.GamePlayer;

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
