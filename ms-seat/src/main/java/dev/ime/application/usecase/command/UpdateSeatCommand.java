package dev.ime.application.usecase.command;

import java.util.UUID;

import dev.ime.domain.command.Command;

public record UpdateSeatCommand(
		UUID seatId,
		String seatNumber,
		UUID planeId
		) implements Command {

}
