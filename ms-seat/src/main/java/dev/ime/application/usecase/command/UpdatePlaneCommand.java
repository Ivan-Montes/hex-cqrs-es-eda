package dev.ime.application.usecase.command;

import java.util.UUID;

import dev.ime.domain.command.Command;

public record UpdatePlaneCommand(
		UUID planeId,
		String name,
		Integer capacity
		) implements Command {

}
