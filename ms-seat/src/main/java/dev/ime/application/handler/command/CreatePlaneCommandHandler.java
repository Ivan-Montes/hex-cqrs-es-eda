package dev.ime.application.handler.command;

import java.util.Optional;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.PlaneCreatedEvent;
import dev.ime.application.usecase.command.CreatePlaneCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.domain.port.outbound.EventNoSqlWriteRepositoryPort;

@Component
public class CreatePlaneCommandHandler implements CommandHandler<Optional<Event>>{

	private final EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort;
	private final DatabaseSequencePort databaseSequencePort;
	
	public CreatePlaneCommandHandler(EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort,
			DatabaseSequencePort databaseSequencePort) {
		super();
		this.eventNoSqlWriteRepositoryPort = eventNoSqlWriteRepositoryPort;
		this.databaseSequencePort = databaseSequencePort;
	}
	
	@Override
	public Optional<Event> handle(Command command) {
		
		if ( command instanceof CreatePlaneCommand createPlaneCommand ) {
			
			PlaneCreatedEvent event = createPlaneCreatedEvent(createPlaneCommand);
			
			return eventNoSqlWriteRepositoryPort.save(event);
			
		}else {
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_COMMAND);
		}	
	}

	private PlaneCreatedEvent createPlaneCreatedEvent(CreatePlaneCommand createPlaneCommand) {
		
		return new PlaneCreatedEvent(
				databaseSequencePort.generateSequence(ApplicationConstant.SEQ_GEN),
				createPlaneCommand.planeId(),
				createPlaneCommand.name(),
				createPlaneCommand.capacity());
				
	}	
	
	
}
