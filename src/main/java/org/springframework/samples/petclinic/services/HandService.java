package org.springframework.samples.petclinic.services;

import org.springframework.beans.factory.annotation.Autowired;
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
    handRepository.save(hand);

  }

  @Transactional(readOnly = true)
  public Iterable<Hand> findAll() {
    return handRepository.findAll();
  }

}
