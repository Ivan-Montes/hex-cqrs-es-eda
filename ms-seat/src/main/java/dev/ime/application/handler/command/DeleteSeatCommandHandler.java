package dev.ime.application.handler.command;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.SeatDeletedEvent;
import dev.ime.application.exception.ReservationAssociatedException;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.command.DeleteSeatCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.model.Seat;
import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.domain.port.outbound.EventNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.GenericNoSqlReadRepositoryPort;
import dev.ime.domain.port.outbound.ReservationRedisProjectorPort;

@Component
public class DeleteSeatCommandHandler implements CommandHandler<Optional<Event>>{

	private final EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort;
	private final DatabaseSequencePort databaseSequencePort;
	private final GenericNoSqlReadRepositoryPort<Seat> genericNoSqlReadRepositoryPort;
	private final ReservationRedisProjectorPort reservationRedisProjectorPort;
	
	public DeleteSeatCommandHandler(EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort,
			DatabaseSequencePort databaseSequencePort,
			GenericNoSqlReadRepositoryPort<Seat> genericNoSqlReadRepositoryPort,
			ReservationRedisProjectorPort reservationRedisProjectorPort) {
		super();
		this.eventNoSqlWriteRepositoryPort = eventNoSqlWriteRepositoryPort;
		this.databaseSequencePort = databaseSequencePort;
		this.genericNoSqlReadRepositoryPort = genericNoSqlReadRepositoryPort;
		this.reservationRedisProjectorPort = reservationRedisProjectorPort;
	}

	@Override
	public Optional<Event> handle(Command command) {
		
	if ( command instanceof DeleteSeatCommand deleteSeatCommand ) {
			
			validateSeatExists(deleteSeatCommand.seatId());
			validateExistsReservationRedisEntityBySeatId(deleteSeatCommand.seatId());
			
			SeatDeletedEvent event = new SeatDeletedEvent(
					databaseSequencePort.generateSequence(ApplicationConstant.SEQ_GEN),
					deleteSeatCommand.seatId()
					);
			
			return eventNoSqlWriteRepositoryPort.save(event);
			
		}else {
			
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_COMMAND);
			
		}		
	}

	private void validateExistsReservationRedisEntityBySeatId(UUID seatId) {
		
		if ( reservationRedisProjectorPort.existsReservationRedisEntityBySeatId(seatId)) {
			
			throw new ReservationAssociatedException(Map.of(ApplicationConstant.SEATID, seatId.toString()));

		}		
	}

	private void validateSeatExists(UUID seatId) {

		if ( genericNoSqlReadRepositoryPort.findById(seatId).isEmpty() ) {
			 
			throw new ResourceNotFoundException(Map.of(ApplicationConstant.SEATID,String.valueOf(seatId)));
			
		}		
	}
	
}
