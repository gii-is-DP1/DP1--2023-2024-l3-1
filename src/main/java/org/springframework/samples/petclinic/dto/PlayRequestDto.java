package org.springframework.samples.petclinic.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayRequestDto {
  @NotNull
  private Integer figure_id;
  
}
