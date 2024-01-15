package com.dobble.dto;

import com.dobble.model.enums.Icon;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
	@NotBlank
	private String email;

	@NotBlank
	private String username;

	@NotBlank
	private String password;

	@Enumerated(EnumType.STRING)
	@NotNull
	private Icon profile_icon = Icon.MANO_LOGO;
}
