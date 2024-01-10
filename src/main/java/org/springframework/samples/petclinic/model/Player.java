package org.springframework.samples.petclinic.model;

import java.util.List;

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
    @JoinColumn(name = "current_game_id")
    Game currentGame;
}
