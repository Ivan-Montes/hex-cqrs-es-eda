package dev.ime.application.dto;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import dev.ime.application.config.ApplicationConstant;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReservationDto(
		UUID reservationId,
		@NotNull UUID clientId,
		@NotNull UUID flightId,
		@NotEmpty @Size(min = 1, max = 5)Set<UUID> seatIdsSet
		) {
	
	public ReservationDto() {
		this(
			UUID.fromString(ApplicationConstant.ZEROUUID),
			UUID.fromString(ApplicationConstant.ZEROUUID), 
			UUID.fromString(ApplicationConstant.ZEROUUID), 
			new HashSet<>()
			);
	}

}
