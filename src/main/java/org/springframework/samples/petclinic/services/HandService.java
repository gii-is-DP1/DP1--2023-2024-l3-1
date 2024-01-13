package org.springframework.samples.petclinic.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Card;
import org.springframework.samples.petclinic.model.Hand;
import org.springframework.samples.petclinic.repositories.HandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class HandService {
  private final HandRepository handRepository;

  @Autowired
  public HandService(HandRepository handRepository) {
    this.handRepository = handRepository;
  }

  @Transactional
  public void saveHand(@Valid Hand hand) {
    this.handRepository.save(hand);
  }

  @Transactional(readOnly = true)
  public Iterable<Hand> findAll() {
    return handRepository.findAll();
  }

  @Transactional
  public Hand createHand(List<Card> cards) {
    Hand h = new Hand();
    h.setCards(cards);
    return this.handRepository.save(h);
  }

  @Transactional
  public void sumStrike(Hand hand) {
    hand.setStrikes(hand.getStrikes() + 1);

    this.handRepository.save(hand);
  }
}
