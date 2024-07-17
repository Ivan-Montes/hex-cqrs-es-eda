package dev.ime.application.handler.command;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.SeatCreatedEvent;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.command.CreateSeatCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.model.Plane;
import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.domain.port.outbound.EventNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.GenericNoSqlReadRepositoryPort;

@Component
public class CreateSeatCommandHandler  implements CommandHandler<Optional<Event>>{

	private final EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort;
	private final DatabaseSequencePort databaseSequencePort;
	private final GenericNoSqlReadRepositoryPort<Plane> planeNoSqlReadRepositoryPort;	

	public CreateSeatCommandHandler(EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort,
			DatabaseSequencePort databaseSequencePort,
			GenericNoSqlReadRepositoryPort<Plane> planeNoSqlReadRepositoryPort) {
		super();
		this.eventNoSqlWriteRepositoryPort = eventNoSqlWriteRepositoryPort;
		this.databaseSequencePort = databaseSequencePort;
		this.planeNoSqlReadRepositoryPort = planeNoSqlReadRepositoryPort;
	}

	@Override
	public Optional<Event> handle(Command command) {
		
		if ( command instanceof CreateSeatCommand createSeatCommand ) {
			
			validatePlaneExists(createSeatCommand.planeId());
			
			SeatCreatedEvent event = createSeatCreatedEvent(createSeatCommand);
			
			return eventNoSqlWriteRepositoryPort.save(event);
			
		}else {
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_COMMAND);
		}	
	}

	private void validatePlaneExists(UUID planeId) {

		if ( planeNoSqlReadRepositoryPort.findById(planeId).isEmpty() ) {
			 
			throw new ResourceNotFoundException(Map.of(ApplicationConstant.PLANEID,String.valueOf(planeId)));
		}	
		
	}

	private SeatCreatedEvent createSeatCreatedEvent(CreateSeatCommand createSeatCommand) {
		
		return new SeatCreatedEvent(
				databaseSequencePort.generateSequence(ApplicationConstant.SEQ_GEN),
				createSeatCommand.seatId(),
				createSeatCommand.seatNumber(),
				createSeatCommand.planeId()
				);
	}
	
}
