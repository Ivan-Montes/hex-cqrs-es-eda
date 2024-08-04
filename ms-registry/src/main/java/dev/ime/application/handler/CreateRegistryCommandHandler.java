package dev.ime.application.handler;

import java.util.Optional;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.RegistryCreatedEvent;
import dev.ime.application.usecase.CreateRegistryCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.domain.port.outbound.EventNoSqlWriteRepositoryPort;

@Component
public class CreateRegistryCommandHandler implements CommandHandler<Optional<Event>>{

	private final EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort;
	private final DatabaseSequencePort databaseSequencePort;	
	
	public CreateRegistryCommandHandler(EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort,
			DatabaseSequencePort databaseSequencePort) {
		super();
		this.eventNoSqlWriteRepositoryPort = eventNoSqlWriteRepositoryPort;
		this.databaseSequencePort = databaseSequencePort;
	}

	@Override
	public Optional<Event> handle(Command command) {
		
		if ( command instanceof CreateRegistryCommand createRegistryCommand ) {
			
			RegistryCreatedEvent event =  createRegistryCreatedEvent(createRegistryCommand);
			
			return eventNoSqlWriteRepositoryPort.save(event);
			
		}else {
			
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_COMMAND);
		
		}	
	}

	private RegistryCreatedEvent createRegistryCreatedEvent(CreateRegistryCommand createRegistryCommand) {
		
		return new RegistryCreatedEvent(
				databaseSequencePort.generateSequence(ApplicationConstant.SEQ_GEN),
				createRegistryCommand.eventData()
				);
	}
	
}
