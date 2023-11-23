package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;
import java.util.Set;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "games")
public class Game {

    @Id
	@GeneratedValue(strategy = GenerationType.UUID)
    protected String id; 

    @Size(min = 3, max = 50)
	@NotBlank
	@Column(name = "name")
	private String name;

    LocalDateTime start;
    LocalDateTime finish;

    @ManyToOne 
    Player creator;

    @ManyToMany
    Set<Player> players;

	@Override
	public String toString() {
		return this.getName();
	}
    
}
