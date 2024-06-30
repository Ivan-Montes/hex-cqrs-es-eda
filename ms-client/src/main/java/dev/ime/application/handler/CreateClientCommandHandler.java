package dev.ime.application.handler;

import java.util.Optional;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.ClientCreatedEvent;
import dev.ime.application.usecase.CreateClientCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.ClientNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.DatabaseSequencePort;

@Component
public class CreateClientCommandHandler implements CommandHandler<Optional<Event>> {
	
	private final ClientNoSqlWriteRepositoryPort clientNoSqlWriteRepositoryPort;
	private final DatabaseSequencePort databaseSequencePort;	
	
	public CreateClientCommandHandler(ClientNoSqlWriteRepositoryPort clientNoSqlWriteRepositoryPort,
			DatabaseSequencePort databaseSequencePort) {
		super();
		this.clientNoSqlWriteRepositoryPort = clientNoSqlWriteRepositoryPort;
		this.databaseSequencePort = databaseSequencePort;
	}

	@Override
	public Optional<Event> handle(Command command) {
		
		if ( command instanceof CreateClientCommand createClientCommand) {
			
			ClientCreatedEvent event = createClientCreatedEvent(createClientCommand);
			
			return clientNoSqlWriteRepositoryPort.save(event);
			
			
		} else {
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_COMMAND);
		}
		
	}
	
	private ClientCreatedEvent createClientCreatedEvent(CreateClientCommand createClientCommand) {
		
		return new ClientCreatedEvent(
				databaseSequencePort.generateSequence(ApplicationConstant.SEQ_GEN),
				createClientCommand.clientId(),
				createClientCommand.name(),
				createClientCommand.lastname());
	}
	
}
