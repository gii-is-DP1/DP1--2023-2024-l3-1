package org.springframework.samples.petclinic.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Figure;

public interface FigureRepository extends CrudRepository<Figure,Integer>{
    List<Figure> findAll(); 
}
