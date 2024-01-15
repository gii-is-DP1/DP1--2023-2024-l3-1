package com.dobble.model;

import com.dobble.model.base.HiddenBaseEntity;
import com.dobble.model.enums.Icon;
import com.dobble.model.enums.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "figures")
public class Figure extends HiddenBaseEntity {
  @Enumerated(EnumType.STRING)
  private Icon icon;

  @ManyToOne
  @NotNull
  @JsonIgnore
  @JoinColumn(name = "card_id")
  private Card card;

  @NotNull
  @Min(0)
  @Max(360)
  private Integer rotation;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Size size;
}
