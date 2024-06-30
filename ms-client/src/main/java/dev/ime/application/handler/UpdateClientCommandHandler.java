package dev.ime.application.handler;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.ClientUpdatedEvent;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.UpdateClientCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.ClientNoSqlReadRepositoryPort;
import dev.ime.domain.port.outbound.ClientNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.DatabaseSequencePort;

@Component
public class UpdateClientCommandHandler implements CommandHandler<Optional<Event>> {

	private final ClientNoSqlWriteRepositoryPort clientNoSqlWriteRepositoryPort;
	private final DatabaseSequencePort databaseSequencePort;
	private final ClientNoSqlReadRepositoryPort clientNoSqlReadRepositoryPort;
	
	public UpdateClientCommandHandler(ClientNoSqlWriteRepositoryPort clientNoSqlWriteRepositoryPort,
			DatabaseSequencePort databaseSequencePort, ClientNoSqlReadRepositoryPort clientNoSqlReadRepositoryPort) {
		super();
		this.clientNoSqlWriteRepositoryPort = clientNoSqlWriteRepositoryPort;
		this.databaseSequencePort = databaseSequencePort;
		this.clientNoSqlReadRepositoryPort = clientNoSqlReadRepositoryPort;
	}

	@Override
	public Optional<Event> handle(Command command) {		

		if ( command instanceof UpdateClientCommand updateClientCommand) {

			final UUID clientId = updateClientCommand.clientId();
			validateClientExists(clientId);
			
			ClientUpdatedEvent event = createClientUpdatedEvent(updateClientCommand);
			
			return clientNoSqlWriteRepositoryPort.save(event);
			
			
		} else {
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_COMMAND);
		}	
		
	}	

	private void validateClientExists(UUID clientId) {
		
		if ( clientNoSqlReadRepositoryPort.findById(clientId).isEmpty() ) {
			 
			throw new ResourceNotFoundException(Map.of(ApplicationConstant.CLIENTID,String.valueOf(clientId)));
		}
		
	}
	
	private ClientUpdatedEvent createClientUpdatedEvent(UpdateClientCommand updateClientCommand) {
		
		return new ClientUpdatedEvent(
				databaseSequencePort.generateSequence(ApplicationConstant.SEQ_GEN),
				updateClientCommand.clientId(),
				updateClientCommand.name(),
				updateClientCommand.lastname());
		
	}
	
}
