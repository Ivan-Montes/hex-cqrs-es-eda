package dev.ime.application.dto;

import java.util.UUID;

import dev.ime.application.config.ApplicationConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public record PlaneDto(
		UUID planeId,
		@NotBlank @Pattern( regexp = ApplicationConstant.PATTERN_NAME_FULL ) String name,
		@PositiveOrZero Integer capacity
		) {

	public PlaneDto() {
		this(UUID.fromString(ApplicationConstant.ZEROUUID), 
				ApplicationConstant.NODATA,
				0);
	}
	
}
