package dev.ime.application.usecase.command;

import java.util.UUID;

import dev.ime.domain.command.Command;

public record DeleteSeatCommand(
		UUID seatId
		) implements Command {

}
