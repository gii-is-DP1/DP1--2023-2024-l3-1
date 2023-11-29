package org.springframework.samples.petclinic.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameCreateDto {
    private String name; 
    
    @Min(value = 2)
    @Max(value = 8)
    private Integer maxPlayers;
}
