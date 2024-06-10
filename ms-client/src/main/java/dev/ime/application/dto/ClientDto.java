package dev.ime.application.dto;

import java.util.UUID;

import dev.ime.application.config.ApplicationConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ClientDto(
		UUID clientId,
		@NotBlank @Pattern( regexp = ApplicationConstant.PATTERN_NAME_FULL ) String name,
		@NotBlank @Pattern( regexp = ApplicationConstant.PATTERN_NAME_FULL ) String lastname
		) {
	
	public ClientDto() {
		this(UUID.fromString(ApplicationConstant.ZEROUUID), ApplicationConstant.NODATA, ApplicationConstant.NODATA);
	}
	
}
