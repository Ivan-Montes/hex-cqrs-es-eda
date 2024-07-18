package dev.ime.application.usecase.command;

import java.util.UUID;

import dev.ime.domain.command.Command;

public record DeletePlaneCommand(
		UUID planeId
		) implements Command {

}
