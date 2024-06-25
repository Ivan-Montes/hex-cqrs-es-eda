package dev.ime.application.handler;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.FlightUpdatedEvent;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.UpdateFlightCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.FlightNoSqlReadRepositoryPort;
import dev.ime.domain.port.outbound.FlightNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.PlaneRedisProjectorPort;
import dev.ime.domain.port.outbound.DatabaseSequencePort;

@Component
public class UpdateFlightCommandHandler implements CommandHandler<Optional<Event>> {

	private final FlightNoSqlWriteRepositoryPort flightNoSqlWriteRepositoryPort;
	private final DatabaseSequencePort databaseSequencePort;
	private final FlightNoSqlReadRepositoryPort flightNoSqlReadRepositoryPort;
	private final PlaneRedisProjectorPort planeRedisProjectorPort;	

	public UpdateFlightCommandHandler(FlightNoSqlWriteRepositoryPort flightNoSqlWriteRepositoryPort,
			DatabaseSequencePort databaseSequencePort, FlightNoSqlReadRepositoryPort flightNoSqlReadRepositoryPort,
			PlaneRedisProjectorPort planeRedisProjectorPort) {
		super();
		this.flightNoSqlWriteRepositoryPort = flightNoSqlWriteRepositoryPort;
		this.databaseSequencePort = databaseSequencePort;
		this.flightNoSqlReadRepositoryPort = flightNoSqlReadRepositoryPort;
		this.planeRedisProjectorPort = planeRedisProjectorPort;
	}

	@Override
	public Optional<Event> handle(Command command) {
		
		if ( command instanceof UpdateFlightCommand updateFlightCommand) {
			
			validateFlightExists(updateFlightCommand.flightId());
			validatePlaneExists(updateFlightCommand.planeId());
			
			FlightUpdatedEvent event = createFlightUpdatedEvent(updateFlightCommand);
			
			return flightNoSqlWriteRepositoryPort.save(event);
			
			
		} else {
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_COMMAND);
		}		
		
	}

	private void validateFlightExists(UUID flightId) {

		if ( flightNoSqlReadRepositoryPort.findById(flightId).isEmpty() ) {
			 
			throw new ResourceNotFoundException(Map.of(ApplicationConstant.FLIGHTID,String.valueOf(flightId)));
		
		}		
	}
	
	private void validatePlaneExists(UUID planeId) {

		if ( !planeRedisProjectorPort.existById(planeId) ) {
			 
			throw new ResourceNotFoundException(Map.of(ApplicationConstant.PLANEID, planeId.toString() ));
		
		}		
	}
	
	private FlightUpdatedEvent createFlightUpdatedEvent(UpdateFlightCommand updateFlightCommand) {
		
		return new FlightUpdatedEvent(
				databaseSequencePort.generateSequence(ApplicationConstant.SEQ_GEN),
				updateFlightCommand.flightId(),
				updateFlightCommand.origin(),
				updateFlightCommand.destiny(),
				updateFlightCommand.departureDate(),
				updateFlightCommand.departureTime(),
				updateFlightCommand.planeId()
				);		
		
	}
	
}
