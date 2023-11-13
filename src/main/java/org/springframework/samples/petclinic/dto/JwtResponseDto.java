package org.springframework.samples.petclinic.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponseDto {

	private String token;
	private String type = "Bearer";
	private Integer id;
	private String username;
	private Boolean is_admin;

	public JwtResponseDto(String accessToken, Integer id, String username, Boolean is_admin) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
		this.is_admin = is_admin;
	}
}
