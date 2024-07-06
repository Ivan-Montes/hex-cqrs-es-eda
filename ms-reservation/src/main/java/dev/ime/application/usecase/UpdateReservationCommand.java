package dev.ime.application.usecase;

import java.util.Set;
import java.util.UUID;

import dev.ime.domain.command.Command;

public record UpdateReservationCommand(
		UUID reservationId,
		UUID clientId,
		UUID flightId,
		Set<UUID> seatIdsSet
		) implements Command {

}
