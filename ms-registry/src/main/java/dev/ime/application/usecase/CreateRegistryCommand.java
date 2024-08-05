package dev.ime.application.usecase;

import java.util.Map;
import java.util.UUID;

import dev.ime.domain.command.Command;

public record CreateRegistryCommand(
		UUID registryId,
	    Map<String, Object> eventData
	    ) implements Command{

}
