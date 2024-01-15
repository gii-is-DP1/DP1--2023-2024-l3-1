package com.dobble.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionMessageDto {
	@NotNull
	private String data;
}
