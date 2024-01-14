package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.Comparator;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.dto.GamePlayerDto;
import org.springframework.samples.petclinic.dto.PublicPlayerDto;
import org.springframework.samples.petclinic.model.base.UUIDEntity;
import org.springframework.samples.petclinic.model.enums.GameStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
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
    @NotBlank
    private String name;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime start;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime finish;

    /**
     * No puede ser null puesto que Spring requiere que GamePlayer exista en la base de datos
     * antes de establecerlo y GamePlayer depende de Game
     */
    @ManyToOne
    @JsonIgnore
    GamePlayer creator;

    @Min(value = 2)
    @Max(value = 8)
    @NotNull
    Integer max_players = 8;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    // El creador va aparte
    @Size(min = 0, max = 7)
    @JsonIgnore
    List<GamePlayer> game_players = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "game")
    private Card initial_card;

    @JsonIgnore
    @Transient
    public boolean isOnLobby() {
        return this.getStart() == null && !isFinished();
    }

    @JsonIgnore
    @Transient
    public boolean isOngoing() {
        return this.getStart() != null && !isFinished();
    }

    @JsonIgnore
    @Transient
    public boolean isFinished() {
        return this.getFinish() != null;
    }

    @Transient
    @JsonProperty("creator")
    public PublicPlayerDto getCreatorDto() {
        return new PublicPlayerDto(this.getCreator().getPlayer());
    }

    @Transient
    @JsonIgnore
    /**
     * GamePlayer incluyendo el creador
     * @return
     */
    public List<GamePlayer> getAllGamePlayers() {
        // Por alguna razón, parece que Spring devuelve al creador aquí también
        // pese a estar en propiedades diferentes. igualmente, mantenemos este método
        // para distinguirlos unívocamente en la lógica de negocio de los servicios.
        this.game_players.sort(Comparator.comparingInt(gpl -> gpl.getNumberOfAvailableCards()));
        return this.getGame_players();
    }

    @Transient
    @JsonIgnore
    public Optional<GamePlayer> getGamePlayerByPlayer(Player player) {
        return this.getAllGamePlayers()
            .stream()
            .filter(gp -> gp.getPlayerId() == player.getId())
            .map(Optional::ofNullable)
            .findFirst()
            .flatMap(Function.identity());
    }

    @Transient
    @JsonIgnore
    public List<Player> getAllPlayers() {
        return this.getAllGamePlayers().stream().map(gp -> gp.getPlayer()).toList();
    }

    @Transient
    @JsonProperty("game_players")
    public List<GamePlayerDto> getGamePlayerDto() {
        return this.getAllGamePlayers().stream().map(gp -> gp.getAsDto()).toList();
    }

    @Transient
    @JsonProperty("central_card")
    public Optional<Card> getCentralCard() {
        if (this.isOngoing()) {
            Optional<Card> potential_central_card = this.getAllGamePlayers()
                .stream()
                .filter(h -> h.getCards() != null)
                .flatMap(h -> h.getCards().stream())
                .filter(c -> c.getRelease_time() != null)
                .sorted(Comparator.comparing(Card::getRelease_time).reversed())
                .map(Optional::ofNullable)
                .findFirst()
                .flatMap(Function.identity());

            return potential_central_card.isPresent() ? potential_central_card : Optional.ofNullable(this.getInitial_card());
        }

        return Optional.empty();
    }

    @Transient
    public GameStatus getStatus() {
        if (this.isOnLobby()) {
            return GameStatus.LOBBY;
        } else if (this.isOngoing()) {
            return GameStatus.STARTED;
        } else {
            return GameStatus.FINISHED;
        }
    }

    @Transient
    public boolean isFull() {
        return this.getAllGamePlayers().size() == this.getMax_players();
    }

    @Transient
    @JsonProperty("startable")
    public Boolean startable() {
        Integer players = this.getAllGamePlayers().size();

        return this.getStatus() == GameStatus.LOBBY && players <= this.getMax_players() && players >= 2;
    }
}
