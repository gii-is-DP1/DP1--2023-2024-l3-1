package com.dobble.repositories;

import java.util.List;
import java.util.Optional;

import com.dobble.model.Achievement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends CrudRepository<Achievement, Integer> {
    List<Achievement> findAll();

    public Optional<Achievement> findByName(String name);
}
