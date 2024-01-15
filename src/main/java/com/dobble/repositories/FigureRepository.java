package com.dobble.repositories;

import java.util.List;

import com.dobble.model.Figure;
import org.springframework.data.repository.CrudRepository;

public interface FigureRepository extends CrudRepository<Figure, Integer> {
    List<Figure> findAll();
}
