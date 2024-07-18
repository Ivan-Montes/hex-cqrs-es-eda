package dev.ime.application.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.dispatch.SeatCommandDispatcher;
import dev.ime.application.dto.SeatDto;
import dev.ime.application.exception.EventUnexpectedException;
import dev.ime.application.usecase.command.CreateSeatCommand;
import dev.ime.application.usecase.command.DeleteSeatCommand;
import dev.ime.application.usecase.command.UpdateSeatCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.inbound.GenericCommandServicePort;
import dev.ime.domain.port.outbound.PublisherPort;

@Service
public class SeatCommandService implements GenericCommandServicePort<SeatDto>{

	private final SeatCommandDispatcher seatCommandDispatcher;
	private final PublisherPort publisherPort;
	
	public SeatCommandService(SeatCommandDispatcher seatCommandDispatcher, PublisherPort publisherPort) {
		super();
		this.seatCommandDispatcher = seatCommandDispatcher;
		this.publisherPort = publisherPort;
	}
	
	@Override
	public Optional<Event> create(SeatDto dto) {
		
		CreateSeatCommand command = new CreateSeatCommand(
				UUID.randomUUID(),
				dto.seatNumber(),
				dto.planeId()
				);
		
		return handleCommand(command);
	}
	
	@Override
	public Optional<Event> update(UUID id, SeatDto dto) {
		
		UpdateSeatCommand command = new UpdateSeatCommand(
				id,
				dto.seatNumber(),
				dto.planeId()
				);

		return handleCommand(command);
	}
	
	@Override
	public Optional<Event> deleteById(UUID id) {
		
		DeleteSeatCommand command = new DeleteSeatCommand(id);

		return handleCommand(command);
	}
	
	private Optional<Event> handleCommand(Command command) {
		
        CommandHandler<Optional<Event>> handler = seatCommandDispatcher.getCommandHandler(command);
        Optional<Event> optEvent =  handler.handle(command);
        checkEventEmpty(optEvent);
        publishEventIfPresent(optEvent);
        
		return optEvent;
		
    }
	
	private void checkEventEmpty(Optional<Event> optEvent) {
		
		if ( optEvent.isEmpty() ) {
			
			throw new EventUnexpectedException(Map.of(ApplicationConstant.CAT_EVE, ApplicationConstant.NODATA));
			
		}
		
	}
	
	private void publishEventIfPresent(Optional<Event> optEvent) {
		
		if ( optEvent.isPresent() ) {
			
			publisherPort.publishEvent(optEvent.get());	
			
		}
		
	}
	
}
