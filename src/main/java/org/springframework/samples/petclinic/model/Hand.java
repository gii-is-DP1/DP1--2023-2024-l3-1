package org.springframework.samples.petclinic.model;

import java.util.List;

import org.springframework.samples.petclinic.model.base.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Hand extends BaseEntity{
  
  //TODO One to One
  @NotNull
  @OneToOne
  private GamePlayer gamePlayer; 

  @NotNull
  @OneToMany
  @Size(max = 8)
  private List<Card> cards; 
  
}
