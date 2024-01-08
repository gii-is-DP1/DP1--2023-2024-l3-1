package org.springframework.samples.petclinic.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Card;

public interface CardRepository extends CrudRepository<Card,Integer>{
    List<Card> findAll(); 

}
