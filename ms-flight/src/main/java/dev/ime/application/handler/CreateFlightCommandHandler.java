package dev.ime.application.handler;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.FlightCreatedEvent;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.CreateFlightCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.FlightNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.PlaneRedisProjectorPort;
import dev.ime.domain.port.outbound.DatabaseSequencePort;

@Component
public class CreateFlightCommandHandler implements CommandHandler<Optional<Event>> {

	private final FlightNoSqlWriteRepositoryPort flightNoSqlWriteRepositoryPort;
	private final DatabaseSequencePort databaseSequencePort;
	private final PlaneRedisProjectorPort planeRedisProjectorPort;	

	public CreateFlightCommandHandler(FlightNoSqlWriteRepositoryPort flightNoSqlWriteRepositoryPort,
			DatabaseSequencePort databaseSequencePort, PlaneRedisProjectorPort planeRedisProjectorPort) {
		super();
		this.flightNoSqlWriteRepositoryPort = flightNoSqlWriteRepositoryPort;
		this.databaseSequencePort = databaseSequencePort;
		this.planeRedisProjectorPort = planeRedisProjectorPort;
	}

	@Override
	public Optional<Event> handle(Command command) {
		
		if ( command instanceof CreateFlightCommand createFlightCommand) {
			
			validatePlaneExists(createFlightCommand.planeId());
			FlightCreatedEvent event = createFlightCreatedEvent(createFlightCommand);
			
			return flightNoSqlWriteRepositoryPort.save(event);
			
			
		} else {
			
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_COMMAND);
		
		}		
	}
	
	private FlightCreatedEvent createFlightCreatedEvent(CreateFlightCommand createFlightCommand) {
		
		return new FlightCreatedEvent(
				databaseSequencePort.generateSequence(ApplicationConstant.SEQ_GEN),
				createFlightCommand.flightId(),
				createFlightCommand.origin(),
				createFlightCommand.destiny(),
				createFlightCommand.departureDate(),
				createFlightCommand.departureTime(),
				createFlightCommand.planeId()
				);
		
	}

	private void validatePlaneExists(UUID planeId) {

		if ( !planeRedisProjectorPort.existById(planeId) ) {
			 
			throw new ResourceNotFoundException(Map.of(ApplicationConstant.PLANEID, planeId.toString() ));
		
		}		
	}
	
}
