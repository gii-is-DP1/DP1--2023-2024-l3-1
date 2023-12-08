package org.springframework.samples.petclinic.dto;

import org.springframework.samples.petclinic.model.enums.Icon;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditPlayerDto {
	private String email;

	private String username;

	private String password;

	private Icon profileIcon;
}
