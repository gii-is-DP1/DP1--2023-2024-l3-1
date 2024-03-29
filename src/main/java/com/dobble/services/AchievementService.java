package com.dobble.services;

import java.util.List;
import java.util.Optional;

import com.dobble.model.Achievement;
import com.dobble.model.enums.AchievementMetric;
import com.dobble.repositories.AchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class AchievementService {

    AchievementRepository repo;

    @Autowired
    public AchievementService(AchievementRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public Optional<List<Achievement>> getAchievements() {
        List<Achievement> achievements = repo.findAll();
        return Optional.ofNullable(achievements);
    }

    @Transactional(readOnly = true)
    public Optional<Achievement> getAchievementById(int id) {
        return repo.findById(id);
    }

    @Transactional
    public Achievement saveAchievement(@Valid Achievement newAchievement) {
        return repo.save(newAchievement);
    }

    @Transactional(rollbackFor = Exception.class)
    public Achievement updateAchievement(@Valid Achievement achievement, Integer idToUpdate) {
        Optional<Achievement> toUpdate_opt = getAchievementById(idToUpdate);

        if (toUpdate_opt.isPresent()) {
            Achievement toUpdate = toUpdate_opt.get();
            String newName = achievement.getName();
            String newDescription = achievement.getDescription();
            String newBadgeImage = achievement.getBadgeImage();
            Double newThreshold = achievement.getThreshold();
            AchievementMetric newMetric = achievement.getMetric();
            if (newName != null && !newName.isBlank()) {
                toUpdate.setName(achievement.getName());
            }
            if (newDescription != null && !newDescription.isBlank()) {
                toUpdate.setDescription(achievement.getDescription());
            }
            if (newBadgeImage != null && !newBadgeImage.isBlank()) {
                toUpdate.setBadgeImage(achievement.getBadgeImage());
            }
            if (newThreshold != null) {
                toUpdate.setThreshold(achievement.getThreshold());
            }
            if (newMetric != null) {
                toUpdate.setMetric(achievement.getMetric());
            }

            this.repo.save(toUpdate);

            return toUpdate;
        }

        return null;
    }

    @Transactional
    public void deleteAchievementById(int id) {
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Achievement> getAchievementByName(String name) {
        return repo.findByName(name);
    }
}
