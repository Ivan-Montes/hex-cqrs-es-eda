package dev.ime.application.dto;

import java.util.UUID;

import dev.ime.application.config.ApplicationConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SeatDto(
		UUID seatId,
		@NotBlank @Pattern( regexp = ApplicationConstant.PATTERN_NAME_FULL ) String seatNumber,
		@NotNull UUID planeId) {

	public SeatDto() {
		this(UUID.fromString(ApplicationConstant.ZEROUUID), 
				ApplicationConstant.NODATA,
				UUID.fromString(ApplicationConstant.ZEROUUID));
	}
	
}
