package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime start;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime finish;

    @NotNull
    @ManyToOne
    Player creator;

    @Min(value = 2)
    @Max(value = 8)
    Integer maxPlayers = 8;
    /*
     * @ManyToMany
     * 
     * @Size(min= 1 ,max = 8)
     * Set<Player> players;
     */

    @OneToMany(mappedBy = "game")
    @Size(min = 1, max = 8)
    Set<GamePlayer> game_players;

    @Override
    public String toString() {
        return this.getName();
    }

    @Transient
    public boolean isOnLobby() {
        return this.start == null && !isFinished();
    }

    @Transient
    public boolean isOngoing() {
        return this.start != null && !isFinished();
    }

    @Transient
    public boolean isFinished() {
        return this.finish != null;
    }
}
