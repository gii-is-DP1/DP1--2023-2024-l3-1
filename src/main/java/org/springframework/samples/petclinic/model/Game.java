package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.model.enums.GameState;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime start;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime finish;

    //@NotNull
    @ManyToOne 
    Player creator;

    //@NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'LOBBY'")
    GameState game_state;

    @ManyToMany
    @Size(min= 1 ,max = 8)
    Set<Player> players;


	@Override
	public String toString() {
		return this.getName();
	}
    

}
