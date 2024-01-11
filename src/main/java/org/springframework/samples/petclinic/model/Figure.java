package org.springframework.samples.petclinic.model;

import org.springframework.samples.petclinic.model.base.BaseEntity;
import org.springframework.samples.petclinic.model.enums.Icon;
import org.springframework.samples.petclinic.model.enums.Size;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Figure extends BaseEntity {
  @Enumerated(EnumType.STRING)
  private Icon icon;

  @NotNull
  private Card card;

  @NotNull
  @Min(0)
  @Max(360)
  private Integer rotation;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Size size;
}
