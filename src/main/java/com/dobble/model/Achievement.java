package com.dobble.model;

import com.dobble.model.base.NamedEntity;
import com.dobble.model.enums.AchievementMetric;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Achievement extends NamedEntity {
    @NotBlank
    private String description;

    private String badgeImage;

    @Min(0)
    private double threshold;

    @Enumerated(EnumType.STRING)
    @NotNull
    AchievementMetric metric;

}
