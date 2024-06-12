package dev.ime.application.usecase;

import java.util.UUID;

import dev.ime.domain.command.Command;

public record CreateClientCommand(
		UUID clientId,
	    String name,
	    String lastname
	    ) implements Command{

}
