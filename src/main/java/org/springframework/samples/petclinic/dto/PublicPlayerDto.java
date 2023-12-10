package org.springframework.samples.petclinic.dto;

import org.springframework.samples.petclinic.model.Player;
import org.springframework.samples.petclinic.model.enums.Icon;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicPlayerDto {
	@NotNull
	private String username;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Icon profile_icon;

	public PublicPlayerDto(Player player) {
		this.setUsername(player.getUsername());
		this.setProfile_icon(player.getProfile_icon());
	}
}
