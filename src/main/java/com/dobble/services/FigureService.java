package com.dobble.services;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.dobble.model.Card;
import com.dobble.model.Figure;
import com.dobble.model.enums.Icon;
import com.dobble.model.enums.Size;
import com.dobble.repositories.FigureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class FigureService {
  private final FigureRepository figureRepository;
  private final Random seed;
  private final Size[] sizes;
  private final Icon[] icons;
  private final Integer size_range;

  @Autowired
  public FigureService(FigureRepository figureRepository) {
    this.figureRepository = figureRepository;
    this.seed = new Random();
    this.sizes = Size.values();
    this.size_range = this.sizes.length;
    this.icons = Icon.values();
  }

  @Transactional(readOnly = true)
  public Optional<List<Figure>> findAll() {
    List<Figure> figures = this.figureRepository.findAll();
    return Optional.ofNullable(figures);
  }

  @Transactional
  public Figure createFigure(@Valid Card card, Integer iconIndex) {
    Figure fig = new Figure();
    // Número aleatorio entre 0 y 360
    fig.setRotation(seed.ints(0, 360).findFirst().getAsInt());
    fig.setSize(sizes[seed.ints(0, this.size_range).findFirst().getAsInt()]);
    fig.setIcon(this.icons[iconIndex]);
    fig.setCard(card);
    this.figureRepository.save(fig);

    return fig;
  }
}
