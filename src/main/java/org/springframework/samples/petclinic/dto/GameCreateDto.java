package org.springframework.samples.petclinic.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameCreateDto {
    @NotNull
    private String name;

    @Min(value = 2)
    @Max(value = 8)
    @NotNull
    private Integer maxPlayers;
}
