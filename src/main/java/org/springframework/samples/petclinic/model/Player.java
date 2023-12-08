package org.springframework.samples.petclinic.model;

import java.util.List;

import org.springframework.samples.petclinic.model.base.BaseEntity;
import org.springframework.samples.petclinic.model.enums.Icon;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
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

    @Enumerated(EnumType.STRING)
    Icon profile_icon;

    @NotNull
    Boolean is_admin = false;

    @ManyToMany
    List<Player> friends;
}
