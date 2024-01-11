package org.springframework.samples.petclinic.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Card;
import org.springframework.samples.petclinic.model.Figure;
import org.springframework.samples.petclinic.repositories.CardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class CardService {
  private final CardRepository cardRepository;
  private final FigureService figureService;

  @Autowired 
  public CardService(CardRepository cardRepository, FigureService figureService){
    this.cardRepository = cardRepository; 
    this.figureService = figureService;
  }

  @Transactional(readOnly = true)
  public Optional<List<Card>> findAll(){
    List<Card> cards = cardRepository.findAll();
    return Optional.ofNullable(cards);
  }

  @Transactional
  public Card saveCard(@Valid Card card) {
    this.cardRepository.save(card);

    return card;
  }

  @Transactional
    public List<Card> createCards() {
        // Variables de inicio
        List<Card> cards = new ArrayList<Card>();
        Integer icons_per_card = 8;
        // Hay 57 símbolos
        Integer total_cards = 57; // N = 8 - 1 -> N^2 + N + 1

        Integer n = icons_per_card - 1;
        Integer[][] c = new Integer[total_cards][icons_per_card];

        // Generar primer set de cartas, empezando siempre por el primer
        // símbolo seguido de n símbolos en su orden secuencial
        for (Integer i = 0; i < (n + 1); i++) {
          c[i][0] = 0;

          for (Integer j = 0; j < n; j++) {
            c[i][j] = (j + 1) + (i * n);
          }
        }

        // Generar set final de cartas, empezando por los símbolos siguientes
        // y seguido por el desplazamiento de los símbolos siguientes
        for (Integer i = 0; i < n; i++) {
          for (Integer j = 0; j < n; j++) {
            c[i][j] = i + 1;

            for (Integer k = 0; k < n; k++) {
              c[i][j] = ((n + 1) + (n * k) + ((i * k) + j) % n);
            }
          }
        }

        // Convertimos todo a entidades
        // Es más rápido hacerlo en otro bucle que si lo hubiéramos hecho en los otros bucles
        // accediendo a las entidades en la base de datos.
        for (Integer i = 0; i < total_cards; i++) {
          Card card = new Card();

          for (Integer j = 0; j < icons_per_card; j++) {
            Figure fig = this.figureService.createFigure(card, c[i][j]);
            card.getFigures().add(fig);
          }
          
          this.saveCard(card);
          cards.add(card);
        }

        return cards;
    }
}
