package org.springframework.samples.petclinic.model;

import java.util.List;
import java.util.Optional;

import org.springframework.samples.petclinic.model.base.BaseEntity;
import org.springframework.samples.petclinic.model.enums.Icon;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "players")
public class Player extends BaseEntity {
    @NotNull
    @NotEmpty
    @Column(unique = true)
    String username;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    String email;

    @NotNull
    @NotEmpty
    @JsonIgnore
    String password;

    @NotNull
    Boolean is_admin = false;

    @Enumerated(EnumType.STRING)
    Icon profile_icon = !this.is_admin ? Icon.MANO_LOGO : null;

    @ManyToMany
    @JsonIgnore
    List<Player> friends;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_player_id")
    List<GamePlayer> game_players;

    public Optional<Game> current_game() {
        return this.game_players.stream()
            .map(gp -> gp.getGame())
            .filter(g -> !g.isFinished())
            .findFirst();
    }
}
