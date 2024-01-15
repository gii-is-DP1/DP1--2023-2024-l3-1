package com.dobble.dto;

import com.dobble.model.enums.Icon;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayRequestDto {
  @NotNull
  private Icon icon;

}
