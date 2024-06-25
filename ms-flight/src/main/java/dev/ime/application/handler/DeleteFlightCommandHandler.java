package dev.ime.application.handler;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.FlightDeletedEvent;
import dev.ime.application.exception.ReservationAssociatedException;
import dev.ime.application.usecase.DeleteFlightCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.domain.port.outbound.FlightNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.ReservationRedisProjectorPort;

@Component
public class DeleteFlightCommandHandler implements CommandHandler<Optional<Event>> {

	private final FlightNoSqlWriteRepositoryPort flightNoSqlWriteRepositoryPort;
	private final DatabaseSequencePort databaseSequencePort;
	private final ReservationRedisProjectorPort reservationRedisProjectorPort;		

	public DeleteFlightCommandHandler(FlightNoSqlWriteRepositoryPort flightNoSqlWriteRepositoryPort,
			DatabaseSequencePort databaseSequencePort, ReservationRedisProjectorPort reservationRedisProjectorPort) {
		super();
		this.flightNoSqlWriteRepositoryPort = flightNoSqlWriteRepositoryPort;
		this.databaseSequencePort = databaseSequencePort;
		this.reservationRedisProjectorPort = reservationRedisProjectorPort;
	}

	@Override
	public Optional<Event> handle(Command command) {		
	
		if ( command instanceof DeleteFlightCommand deleteFlightCommand) {
			
			validateFlighIsInAnyReservation(deleteFlightCommand.flightId());
			
			FlightDeletedEvent event = new FlightDeletedEvent(
					databaseSequencePort.generateSequence(ApplicationConstant.SEQ_GEN),
					deleteFlightCommand.flightId()
					);
			
			return flightNoSqlWriteRepositoryPort.save(event);

		}else {
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_COMMAND);
		}
	
	}


	private void validateFlighIsInAnyReservation(UUID flightId) {
		
		if ( reservationRedisProjectorPort.existsReservationRedisEntityByFlighId(flightId)) {
			
			throw new ReservationAssociatedException(Map.of(ApplicationConstant.FLIGHTID, flightId.toString()));

		}		
	}
	
}
