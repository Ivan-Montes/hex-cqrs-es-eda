package dev.ime.application.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.dispatch.PlaneCommandDispatcher;
import dev.ime.application.dto.PlaneDto;
import dev.ime.application.exception.EventUnexpectedException;
import dev.ime.application.usecase.command.CreatePlaneCommand;
import dev.ime.application.usecase.command.DeletePlaneCommand;
import dev.ime.application.usecase.command.UpdatePlaneCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.inbound.GenericCommandServicePort;
import dev.ime.domain.port.outbound.PublisherPort;

@Service
public class PlaneCommandService implements GenericCommandServicePort<PlaneDto>{

	private final PlaneCommandDispatcher planeCommandDispatcher;
	private final PublisherPort publisherPort;
	
	public PlaneCommandService(PlaneCommandDispatcher planeCommandDispatcher, PublisherPort publisherPort) {
		super();
		this.planeCommandDispatcher = planeCommandDispatcher;
		this.publisherPort = publisherPort;
	}

	@Override
	public Optional<Event> create(PlaneDto dto) {
		
		CreatePlaneCommand command = new CreatePlaneCommand(
				UUID.randomUUID(),
				dto.name(),
				dto.capacity()
				);
				
		return handleCommand(command);
	}

	@Override
	public Optional<Event> update(UUID id, PlaneDto dto) {
		
		UpdatePlaneCommand command = new UpdatePlaneCommand(
				id,
				dto.name(),
				dto.capacity()
				);
		
		return handleCommand(command);
	}

	@Override
	public Optional<Event> deleteById(UUID id) {
		
		DeletePlaneCommand command = new DeletePlaneCommand(id);
		
		return handleCommand(command);
	}
	
	private Optional<Event> handleCommand(Command command) {
		
        CommandHandler<Optional<Event>> handler = planeCommandDispatcher.getCommandHandler(command);
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
