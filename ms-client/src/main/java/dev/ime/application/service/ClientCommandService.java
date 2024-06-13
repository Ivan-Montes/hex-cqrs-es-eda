package dev.ime.application.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.dispatch.ClientCommandDispatcher;
import dev.ime.application.dto.ClientDto;
import dev.ime.application.exception.EventUnexpectedException;
import dev.ime.application.usecase.CreateClientCommand;
import dev.ime.application.usecase.DeleteClientCommand;
import dev.ime.application.usecase.UpdateClientCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.inbound.ClientCommandServicePort;
import dev.ime.domain.port.outbound.PublisherPort;

@Service
public class ClientCommandService implements ClientCommandServicePort<ClientDto>{

	private final ClientCommandDispatcher clientCommandDispatcher;
	private final PublisherPort publisherPort;
	
	public ClientCommandService(ClientCommandDispatcher clientCommandDispatcher, PublisherPort publisherPort) {
		super();
		this.clientCommandDispatcher = clientCommandDispatcher;
		this.publisherPort = publisherPort;
	}

	@Override
	public Optional<Event> create(ClientDto dto) {

		CreateClientCommand createCommand = new CreateClientCommand(UUID.randomUUID(), dto.name(), dto.lastname());
		return handleCommand(createCommand);
		
	}
	
	@Override
	public Optional<Event> update(UUID id, ClientDto dto) {
		
		UpdateClientCommand updateCommand = new UpdateClientCommand(id, dto.name(), dto.lastname());
		return handleCommand(updateCommand);
		
	}
	
	@Override
	public Optional<Event> deleteById(UUID id) {
		
		DeleteClientCommand deleteCommand = new DeleteClientCommand(id);
		return handleCommand(deleteCommand);
		
	}

	private Optional<Event> handleCommand(Command command) {
		
        CommandHandler<Optional<Event>> handler = clientCommandDispatcher.getCommandHandler(command);
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
