package org.springframework.samples.petclinic.dto;

import org.springframework.samples.petclinic.model.enums.Icon;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditPlayerDto {
	private String email;

	private String username;

	private String password;

	@Enumerated(EnumType.STRING)
	private Icon profile_icon;
}
