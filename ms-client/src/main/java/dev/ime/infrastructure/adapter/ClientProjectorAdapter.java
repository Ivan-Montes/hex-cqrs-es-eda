package dev.ime.infrastructure.adapter;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.ClientCreatedEvent;
import dev.ime.application.event.ClientDeletedEvent;
import dev.ime.application.event.ClientUpdatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.ClientProjectorPort;
import dev.ime.infrastructure.entity.ClientMongoEntity;
import dev.ime.infrastructure.repository.read.ClientNoSqlReadRepository;

@Repository
public class ClientProjectorAdapter implements ClientProjectorPort{

	private final ClientNoSqlReadRepository clientNoSqlReadRepository;
	private final LoggerUtil loggerUtil;
	
	public ClientProjectorAdapter(ClientNoSqlReadRepository clientNoSqlReadRepository,
			LoggerUtil loggerUtil) {
		super();
		this.clientNoSqlReadRepository = clientNoSqlReadRepository;
		this.loggerUtil = loggerUtil;
	}

	@Override
	public void create(Event event) {
		
		if ( event instanceof ClientCreatedEvent clientCreatedEvent) {
			
			ClientMongoEntity clientMongoEntity = buildClientEntity(clientCreatedEvent);
			
			saveClientEntity(clientMongoEntity);
			
			logInfo("ClientCreatedEvent", clientMongoEntity.toString());
		}
		
	}

	@Override
	public void update(Event event) {

		if ( event instanceof ClientUpdatedEvent clientUpdatedEvent) {
			
			UUID clientId = clientUpdatedEvent.getClientId();			
			Optional<ClientMongoEntity>optClientFound = clientNoSqlReadRepository.findFirstByClientId(clientId);	
			
			if ( optClientFound.isEmpty() ) {
				
				logInfo("ClientUpdatedEvent] -> [ResourceNotFoundException", ApplicationConstant.CLIENTID + " : " + clientId );
				return;
			}

			ClientMongoEntity clientFound = optClientFound.get();
			clientFound.setName(clientUpdatedEvent.getName());			
			clientFound.setLastname(clientUpdatedEvent.getLastname());
			saveClientEntity(clientFound);	
			
			logInfo("ClientUpdatedEvent", clientFound.toString());

		}	
		
	}

	@Override
	public void deleteById(Event event) {

		if ( event instanceof ClientDeletedEvent clientDeletedEvent) {
			
			UUID id = clientDeletedEvent.getClientId();
			Optional<ClientMongoEntity>optClientFound = clientNoSqlReadRepository.findFirstByClientId(id);	
			
			if ( optClientFound.isEmpty() ) {
				
				logInfo("ClientDeletedEvent] -> [ResourceNotFoundException", ApplicationConstant.CLIENTID + " : "  + id);
				return;
			}
			
			clientNoSqlReadRepository.delete(optClientFound.get());
			
			logInfo("ClientDeletedEvent", ApplicationConstant.CLIENTID + " : "  + id);

		}
		
	}

	private ClientMongoEntity buildClientEntity(ClientCreatedEvent clientCreatedEvent) {
		
	    ClientMongoEntity clientMongoEntity = new ClientMongoEntity();
	    clientMongoEntity.setClientId(clientCreatedEvent.getClientId());
	    clientMongoEntity.setName(clientCreatedEvent.getName());
	    clientMongoEntity.setLastname(clientCreatedEvent.getLastname());
	    
	    return clientMongoEntity;

	}
	
	private void saveClientEntity(ClientMongoEntity clientMongoEntity) {
		
	    clientNoSqlReadRepository.save(clientMongoEntity);
	}
	
	private void logInfo(String action, String entityInfo) {
		
	    loggerUtil.logInfoAction(this.getClass().getSimpleName(), action, entityInfo);
	    
	}

}
