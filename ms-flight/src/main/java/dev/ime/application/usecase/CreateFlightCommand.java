package dev.ime.application.usecase;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import dev.ime.domain.command.Command;

public record CreateFlightCommand(
		UUID flightId,
		String origin,
		String destiny,
		LocalDate departureDate,
		LocalTime departureTime,
		UUID planeId
		) implements Command {

}
