package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.samples.petclinic.model.base.BaseEntity;
import org.springframework.samples.petclinic.model.enums.Icon;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "figures")
public class Figure extends BaseEntity {
  @Enumerated(EnumType.STRING)
  private Icon icon;

  @JsonIgnore
  @ManyToMany(mappedBy = "figures")
  private List<Card> cards = new ArrayList<>();

}
