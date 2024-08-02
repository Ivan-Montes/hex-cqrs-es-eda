package dev.ime.application.dto;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dev.ime.application.config.ApplicationConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistryDto(
		UUID eventId,
		@NotBlank String eventCategory,
		@NotBlank String eventType,
		@NotBlank Instant timeInstant,
		@NotNull Long sequence,
		@NotBlank Map<String, Object> eventData) {

	public RegistryDto() {
		this(
				UUID.fromString(ApplicationConstant.ZEROUUID),
				ApplicationConstant.NODATA,
				ApplicationConstant.NODATA,
				Instant.now(),
				0L,
				new HashMap<>()
				);
	}
}
