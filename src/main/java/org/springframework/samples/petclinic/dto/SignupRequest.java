package org.springframework.samples.petclinic.dto;

import jakarta.validation.constraints.NotBlank;

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
}