package dev.ime.application.handler.command;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.PlaneUpdatedEvent;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.command.UpdatePlaneCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.model.Plane;
import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.domain.port.outbound.EventNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.GenericNoSqlReadRepositoryPort;

@Component
public class UpdatePlaneCommandHandler implements CommandHandler<Optional<Event>>{

	private final EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort;
	private final DatabaseSequencePort databaseSequencePort;
	private final GenericNoSqlReadRepositoryPort<Plane> genericNoSqlReadRepositoryPort;

	public UpdatePlaneCommandHandler(EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort,
			DatabaseSequencePort databaseSequencePort,
			GenericNoSqlReadRepositoryPort<Plane> genericNoSqlReadRepositoryPort) {
		super();
		this.eventNoSqlWriteRepositoryPort = eventNoSqlWriteRepositoryPort;
		this.databaseSequencePort = databaseSequencePort;
		this.genericNoSqlReadRepositoryPort = genericNoSqlReadRepositoryPort;
	}

	@Override
	public Optional<Event> handle(Command command) {
		
		if ( command instanceof UpdatePlaneCommand updatePlaneCommand ) {
			
			validatePlaneExists(updatePlaneCommand.planeId());
			
			PlaneUpdatedEvent event = createPlaneUpdatedEvent(updatePlaneCommand);
			
			return eventNoSqlWriteRepositoryPort.save(event);
			
		}else {
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_COMMAND);
		}	
	}	
	
	
	private void validatePlaneExists(UUID planeId) {

		if ( genericNoSqlReadRepositoryPort.findById(planeId).isEmpty() ) {
			 
			throw new ResourceNotFoundException(Map.of(ApplicationConstant.PLANEID,String.valueOf(planeId)));
		}	
		
	}

	private PlaneUpdatedEvent createPlaneUpdatedEvent(UpdatePlaneCommand updatePlaneCommand) {
		
		return new PlaneUpdatedEvent(
				databaseSequencePort.generateSequence(ApplicationConstant.SEQ_GEN),
				updatePlaneCommand.planeId(),
				updatePlaneCommand.name(),
				updatePlaneCommand.capacity());
				
	}

}
