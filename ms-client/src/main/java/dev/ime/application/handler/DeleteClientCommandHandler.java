package dev.ime.application.handler;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.ClientDeletedEvent;
import dev.ime.application.exception.ReservationAssociatedException;
import dev.ime.application.usecase.DeleteClientCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.ClientNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.domain.port.outbound.ReservationRedisProjectorPort;

@Component
public class DeleteClientCommandHandler implements CommandHandler<Optional<Event>> {

	private final ClientNoSqlWriteRepositoryPort clientNoSqlWriteRepositoryPort;
	private final DatabaseSequencePort databaseSequencePort;
	private final ReservationRedisProjectorPort reservationRedisProjectorPort;		

	public DeleteClientCommandHandler(ClientNoSqlWriteRepositoryPort clientNoSqlWriteRepositoryPort,
			DatabaseSequencePort databaseSequencePort, ReservationRedisProjectorPort reservationRedisProjectorPort) {
		super();
		this.clientNoSqlWriteRepositoryPort = clientNoSqlWriteRepositoryPort;
		this.databaseSequencePort = databaseSequencePort;
		this.reservationRedisProjectorPort = reservationRedisProjectorPort;
	}

	@Override
	public Optional<Event>  handle(Command command) {
		
		if ( command instanceof DeleteClientCommand deleteClientCommand) {
			
			validateClientHasAnyReservation(deleteClientCommand.clientId());
			
			ClientDeletedEvent event = new ClientDeletedEvent(
					databaseSequencePort.generateSequence(ApplicationConstant.SEQ_GEN),
					deleteClientCommand.clientId());
			
			return clientNoSqlWriteRepositoryPort.save(event);
			
		} else {
			
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_COMMAND);
	
		}
		
	}

	private void validateClientHasAnyReservation(UUID clientId) {
		
		if ( reservationRedisProjectorPort.existsReservationRedisEntityByClientId(clientId)) {
			
			throw new ReservationAssociatedException(Map.of(ApplicationConstant.CLIENTID, clientId.toString()));

		}		
	}
	
}
