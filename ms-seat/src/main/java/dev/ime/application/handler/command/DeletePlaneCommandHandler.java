package dev.ime.application.handler.command;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.PlaneDeletedEvent;
import dev.ime.application.exception.EntityAssociatedException;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.command.DeletePlaneCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.model.Plane;
import dev.ime.domain.model.Seat;
import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.domain.port.outbound.EventNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.FlightRedisProjectorPort;
import dev.ime.domain.port.outbound.GenericNoSqlReadRepositoryPort;
import dev.ime.domain.port.outbound.SeatSpecificNoSqlReadRepositoryPort;

@Component
public class DeletePlaneCommandHandler implements CommandHandler<Optional<Event>>{

	private final EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort;
	private final DatabaseSequencePort databaseSequencePort;
	private final GenericNoSqlReadRepositoryPort<Plane> genericNoSqlReadRepositoryPort;
	private final SeatSpecificNoSqlReadRepositoryPort<Seat> seatSpecificNoSqlReadRepositoryPort;	
	private final FlightRedisProjectorPort flightRedisProjectorPort;

	public DeletePlaneCommandHandler(EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort,
			DatabaseSequencePort databaseSequencePort,
			GenericNoSqlReadRepositoryPort<Plane> genericNoSqlReadRepositoryPort,
			SeatSpecificNoSqlReadRepositoryPort<Seat> seatSpecificNoSqlReadRepositoryPort,
			FlightRedisProjectorPort flightRedisProjectorPort) {
		super();
		this.eventNoSqlWriteRepositoryPort = eventNoSqlWriteRepositoryPort;
		this.databaseSequencePort = databaseSequencePort;
		this.genericNoSqlReadRepositoryPort = genericNoSqlReadRepositoryPort;
		this.seatSpecificNoSqlReadRepositoryPort = seatSpecificNoSqlReadRepositoryPort;
		this.flightRedisProjectorPort = flightRedisProjectorPort;
	}

	@Override
	public Optional<Event> handle(Command command) {		
	
		if ( command instanceof DeletePlaneCommand deletePlaneCommand) {
			
			UUID planeId = deletePlaneCommand.planeId();
			validatePlaneExists(planeId);
			validatePlaneAssociatedItems(planeId);
			validateFlightAssociatedItem(planeId);
			
			PlaneDeletedEvent event = new PlaneDeletedEvent(
					databaseSequencePort.generateSequence(ApplicationConstant.SEQ_GEN),
					planeId
					);
			
			return eventNoSqlWriteRepositoryPort.save(event);
	
		}else {
			
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_COMMAND);
		
		}	
	}
	
	private void validateFlightAssociatedItem(UUID planeId) {

		if ( flightRedisProjectorPort.existFlightRedisEntityByPlaneId(planeId) ) {
			 
			throw new EntityAssociatedException(Map.of(ApplicationConstant.PLANEID,String.valueOf(planeId)));
	
		}
	}

	private void validatePlaneExists(UUID planeId) {
		
		if ( genericNoSqlReadRepositoryPort.findById(planeId).isEmpty() ) {
			 
			throw new ResourceNotFoundException(Map.of(ApplicationConstant.PLANEID,String.valueOf(planeId)));
	
		}		
	}
	
	private void validatePlaneAssociatedItems(UUID planeId) {
		
		if ( !seatSpecificNoSqlReadRepositoryPort.findByPlaneId(planeId).isEmpty() ) {
			
			throw new EntityAssociatedException(Map.of(ApplicationConstant.PLANEID, String.valueOf(planeId)));
			
		}		
	}
	
}
