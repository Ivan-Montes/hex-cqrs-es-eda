package dev.ime.application.handler;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.ReservationDeletedEvent;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.DeleteReservationCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.model.Reservation;
import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.domain.port.outbound.EventNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.GenericReadRepositoryPort;

@Component
public class DeleteReservationCommandHandler implements CommandHandler<Optional<Event>>{

	private final EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort;
	private final DatabaseSequencePort databaseSequencePort;
	private final GenericReadRepositoryPort<Reservation> genericNoSqlReadRepositoryPort;
	
	public DeleteReservationCommandHandler(EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort,
			DatabaseSequencePort databaseSequencePort,
			GenericReadRepositoryPort<Reservation> genericNoSqlReadRepositoryPort) {
		super();
		this.eventNoSqlWriteRepositoryPort = eventNoSqlWriteRepositoryPort;
		this.databaseSequencePort = databaseSequencePort;
		this.genericNoSqlReadRepositoryPort = genericNoSqlReadRepositoryPort;
	}

	@Override
	public Optional<Event> handle(Command command) {
		
		if ( command instanceof DeleteReservationCommand deleteReservationCommand ) {
			
			validateReservationExists(deleteReservationCommand.reservationId());
			
			ReservationDeletedEvent event = new ReservationDeletedEvent(
					databaseSequencePort.generateSequence(ApplicationConstant.SEQ_GEN),
					deleteReservationCommand.reservationId()					
					);
			
			return eventNoSqlWriteRepositoryPort.save(event);
			
		}else {
			
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_COMMAND);
		
		}
	}

	private void validateReservationExists(UUID reservationId) {

		if ( genericNoSqlReadRepositoryPort.findById(reservationId).isEmpty() ) {
			 
			throw new ResourceNotFoundException(Map.of(ApplicationConstant.RESERVID, reservationId.toString()));
		
		}		
	}
	
}
