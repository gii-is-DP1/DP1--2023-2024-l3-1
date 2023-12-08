package org.springframework.samples.petclinic.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class JwtResponseDto {
	@NotNull
	private String token;
	@NotNull
	private String type = "Bearer";
	@NotNull
	private Integer id;
	@NotNull
	private String username;
	@NotNull
	private Boolean is_admin;

	public JwtResponseDto(String accessToken, Integer id, String username, Boolean is_admin) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
		this.is_admin = is_admin;
	}
}
