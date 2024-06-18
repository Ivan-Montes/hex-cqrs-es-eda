package dev.ime.application.usecase;

import java.util.UUID;

import dev.ime.domain.command.Command;

public record DeleteFlightCommand( UUID flightId) implements Command{

}
