package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.dto.PublicPlayerDto;
import org.springframework.samples.petclinic.model.base.UUIDEntity;
import org.springframework.samples.petclinic.model.enums.GameStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
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
public class Game extends UUIDEntity {
    @Size(min = 3, max = 50)
    @NotBlank
    private String name;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime start;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime finish;

    @NotNull
    @ManyToOne
    @JsonIgnore
    Player raw_creator;

    @Min(value = 2)
    @Max(value = 8)
    @NotNull
    Integer max_players = 8;

    @ManyToMany
    @Size(min = 1, max = 8)
    @JsonIgnore
    List<Player> raw_players;

    @OneToMany(mappedBy = "game")
    @Size(min = 1, max = 8)
    
    List<GamePlayer> raw_game_players;


    @JsonIgnore
    @Transient
    public boolean isOnLobby() {
        return this.start == null && !isFinished();
    }

    @JsonIgnore
    @Transient
    public boolean isOngoing() {
        return this.start != null && !isFinished();
    }

    @JsonIgnore
    @Transient
    public boolean isFinished() {
        return this.finish != null;
    }


    @Transient
    @NotNull
    public PublicPlayerDto getCreator() {
        return new PublicPlayerDto(this.raw_creator);
    }

    @Transient
    @NotNull
    public List<PublicPlayerDto> getPlayers() {
        return this.raw_players.stream()
            .map(p -> new PublicPlayerDto(p))
            .toList();
    }

    @Transient
    @NotNull
    public GameStatus getStatus() {
        if (this.isOnLobby()) {
            return GameStatus.LOBBY;
        } else if (this.isOngoing()) {
            return GameStatus.STARTED;
        } else {
            return GameStatus.FINISHED;
        }
    }
}
