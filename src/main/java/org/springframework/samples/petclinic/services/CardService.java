package org.springframework.samples.petclinic.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
  public void deleteAll(Iterable<? extends Card> entities) {
    this.cardRepository.deleteAll(entities);
  }

  @Transactional
    public List<Card> createCards() {
        // Variables de inicio
        List<Card> cards = new ArrayList<Card>();
        Integer icons_per_card = 8;
        // Hay 57 símbolos
        Integer total_cards = 57; // N = 8 - 1 -> N^2 + N + 1

        Integer n = icons_per_card - 1;
        Integer[] int_stream = IntStream.rangeClosed(0, total_cards - 1).boxed().toArray(Integer[]::new);
        List<Integer> symbols = Arrays.asList(int_stream);
        Collections.shuffle(symbols);
        List<List<Integer>> symbol_cards = new ArrayList<List<Integer>>();

        // Generar primer set de cartas, empezando siempre por el primer
        // símbolo seguido de n símbolos en su orden secuencial
        for (Integer i = 0; i < (n + 1); i++) {
          List<Integer> symbol_card = new ArrayList<Integer>();
          symbol_card.add(symbols.get(0));

          for (Integer j = 0; j < n; j++) {
            symbol_card.add(symbols.get((j + 1) + (i * n)));
          }

          symbol_cards.add(symbol_card);
        }

        // Generar set final de cartas, empezando por los símbolos siguientes
        // y seguido por el desplazamiento de los símbolos siguientes
        for (Integer i = 0; i < n; i++) {
          for (Integer j = 0; j < n; j++) {
            List<Integer> symbol_card = new ArrayList<Integer>();
            symbol_card.add(symbols.get(i + 1));

            for (Integer k = 0; k < n; k++) {
              symbol_card.add(symbols.get(((n + 1) + (n * k) + ((i * k) + j) % n)));
            }

            symbol_cards.add(symbol_card);
          }
        }

        // Convertimos todo a entidades
        // Es más rápido hacerlo en otro bucle que si lo hubiéramos hecho en los otros bucles
        // accediendo a las entidades en la base de datos.
        for (List<Integer> c : symbol_cards) {
          Card card = new Card();
          this.saveCard(card);

          for (Integer f : c) {
            Figure fig = this.figureService.createFigure(card, f);
            card.getFigures().add(fig);
          }

          this.saveCard(card);
          cards.add(card);
        }

        return cards;
    }
}
