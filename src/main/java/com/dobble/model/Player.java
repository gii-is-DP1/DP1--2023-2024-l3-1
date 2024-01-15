package com.dobble.model;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.dobble.model.base.BaseEntity;
import com.dobble.model.enums.Icon;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    List<GamePlayer> game_players;

    @Transient
    @JsonIgnore
    public Optional<Game> current_game() {
        return this.getGame_players()
                .stream()
                .map(gp -> gp.getGame())
                .filter(g -> !g.isFinished())
                .map(Optional::ofNullable)
                .findFirst()
                .flatMap(Function.identity());
    }
}
