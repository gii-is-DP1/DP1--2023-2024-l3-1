package org.springframework.samples.petclinic.model;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.samples.petclinic.model.base.BaseEntity;
import org.springframework.samples.petclinic.model.enums.Icon;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
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

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JsonIgnore
    List<Player> friends;

    @JsonIgnore
    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER, orphanRemoval = true)
    @Fetch(FetchMode.SELECT)
    List<GamePlayer> game_players;

    public Optional<Game> current_game() {
        return this.game_players.stream()
            .map(gp -> gp.getGame())
            .filter(g -> !g.isFinished())
            .map(Optional::ofNullable)
            .findFirst()
            .flatMap(Function.identity());
    }
}
