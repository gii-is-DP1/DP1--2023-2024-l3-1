package com.dobble.repositories;

import java.util.List;

import com.dobble.model.Card;
import org.springframework.data.repository.CrudRepository;

public interface CardRepository extends CrudRepository<Card, Integer> {
    List<Card> findAll();

}
