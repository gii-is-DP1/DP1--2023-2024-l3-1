package org.springframework.samples.petclinic.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Hand;

public interface HandRepository extends CrudRepository<Hand,Integer> {
  
}
