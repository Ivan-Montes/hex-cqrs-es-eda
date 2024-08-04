package dev.ime.application.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.ime.application.dispatch.RegistryCommandDispatcher;
import dev.ime.application.usecase.CreateRegistryCommand;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.inbound.GenericCommandServicePort;
import dev.ime.domain.port.outbound.PublisherPort;

@Service
public class RegistryCommandService implements GenericCommandServicePort<Event>{

	private final RegistryCommandDispatcher registryCommandDispatcher;
	private final PublisherPort publisherPort;
	private final ObjectMapper objectMapper;
	private final LoggerUtil loggerUtil;

	public RegistryCommandService(RegistryCommandDispatcher registryCommandDispatcher, PublisherPort publisherPort,
			ObjectMapper objectMapper, LoggerUtil loggerUtil) {
		super();
		this.registryCommandDispatcher = registryCommandDispatcher;
		this.publisherPort = publisherPort;
		this.objectMapper = objectMapper;
		this.loggerUtil = loggerUtil;
	}

	@Override
	public void create(Event entity) {		
		
		Map<String, Object> eventDataMap = objectMapper.convertValue(entity, new TypeReference<Map<String, Object>>() {});

		CreateRegistryCommand command = new CreateRegistryCommand(
				UUID.randomUUID(),
				eventDataMap
				);
		
		handleCommand(command);		
	}

	private void handleCommand(Command command) {
		
        CommandHandler<Optional<Event>> handler = registryCommandDispatcher.getCommandHandler(command);
        Optional<Event> optEvent =  handler.handle(command);
        publishEventIfPresent(optEvent);        
        logEventIfPresent(optEvent);        
		
    }	
	
	private void publishEventIfPresent(Optional<Event> optEvent) {
		
		if ( optEvent.isPresent() ) {
			
			publisherPort.publishEvent(optEvent.get());	
			
		}		
	}

	private void logEventIfPresent(Optional<Event> optEvent) {
		
		if ( optEvent.isPresent() ) {
			
		    loggerUtil.logInfoAction(this.getClass().getSimpleName(), "handleCommand", optEvent.get().toString());
			
		}		
	}
	
}
