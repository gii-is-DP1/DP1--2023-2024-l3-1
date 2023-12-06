package org.springframework.samples.petclinic.model;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class GamePlayerId implements Serializable {
  private Long game;
  private Long player;

}
