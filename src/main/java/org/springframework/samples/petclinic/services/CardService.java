package org.springframework.samples.petclinic.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Card;
import org.springframework.samples.petclinic.repositories.CardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardService {
  private final CardRepository cardRepository; 

  @Autowired 
  public CardService(CardRepository cardRepository){
    this.cardRepository = cardRepository; 
  }

  @Transactional
  public Optional<List<Card>> findAll(){
    List<Card> cards = cardRepository.findAll();
    return Optional.ofNullable(cards);
  }

}
