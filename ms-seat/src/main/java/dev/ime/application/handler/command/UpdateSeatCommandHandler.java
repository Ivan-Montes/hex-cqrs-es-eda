package dev.ime.application.handler.command;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.SeatUpdatedEvent;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.command.UpdateSeatCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.model.Plane;
import dev.ime.domain.model.Seat;
import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.domain.port.outbound.EventNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.GenericNoSqlReadRepositoryPort;

@Component
public class UpdateSeatCommandHandler implements CommandHandler<Optional<Event>>{

	private final EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort;
	private final DatabaseSequencePort databaseSequencePort;
	private final GenericNoSqlReadRepositoryPort<Seat> seatNoSqlReadRepositoryPort;
	private final GenericNoSqlReadRepositoryPort<Plane> planeNoSqlReadRepositoryPort;
	
	public UpdateSeatCommandHandler(EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort,
			DatabaseSequencePort databaseSequencePort, GenericNoSqlReadRepositoryPort<Seat> seatNoSqlReadRepositoryPort,
			GenericNoSqlReadRepositoryPort<Plane> planeNoSqlReadRepositoryPort) {
		super();
		this.eventNoSqlWriteRepositoryPort = eventNoSqlWriteRepositoryPort;
		this.databaseSequencePort = databaseSequencePort;
		this.seatNoSqlReadRepositoryPort = seatNoSqlReadRepositoryPort;
		this.planeNoSqlReadRepositoryPort = planeNoSqlReadRepositoryPort;
	}

	@Override
	public Optional<Event> handle(Command command) {
		
		if ( command instanceof UpdateSeatCommand updateSeatCommand ) {
			
			validateSeatExists(updateSeatCommand.seatId());
			validatePlaneExists(updateSeatCommand.planeId());
			
			SeatUpdatedEvent event = createSeatUpdatedEvent(updateSeatCommand);
			
			return eventNoSqlWriteRepositoryPort.save(event);
			
		}else {
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_COMMAND);
		}
	
	}

	private void validateSeatExists(UUID seatId) {

		if ( seatNoSqlReadRepositoryPort.findById(seatId).isEmpty() ) {
			 
			throw new ResourceNotFoundException(Map.of(ApplicationConstant.SEATID,String.valueOf(seatId)));
		}
		
	}

	private void validatePlaneExists(UUID planeId) {

		if ( planeNoSqlReadRepositoryPort.findById(planeId).isEmpty() ) {
			 
			throw new ResourceNotFoundException(Map.of(ApplicationConstant.PLANEID,String.valueOf(planeId)));
		}	
		
	}

	private SeatUpdatedEvent createSeatUpdatedEvent(UpdateSeatCommand updateSeatCommand) {
		
		return new SeatUpdatedEvent(
				databaseSequencePort.generateSequence(ApplicationConstant.SEQ_GEN),
				updateSeatCommand.seatId(),
				updateSeatCommand.seatNumber(),
				updateSeatCommand.planeId()
				);
	}
	
}
