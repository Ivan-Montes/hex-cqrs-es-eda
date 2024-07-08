package dev.ime.infrastructure.adapter;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.ClientCreatedEvent;
import dev.ime.application.event.ClientDeletedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.BaseProjectorPort;
import dev.ime.infrastructure.repository.ClientRedisRepository;
import dev.ime.infrastructure.entity.ClientRedisEntity;

@Repository
@Qualifier("clientRedisProjectorAdapter")
public class ClientRedisProjectorAdapter implements BaseProjectorPort{

	private final LoggerUtil loggerUtil;
	private final ClientRedisRepository clientRedisRepository;
	
	public ClientRedisProjectorAdapter(LoggerUtil loggerUtil, ClientRedisRepository clientRedisRepository) {
		super();
		this.loggerUtil = loggerUtil;
		this.clientRedisRepository = clientRedisRepository;
	}
	
	@Override
	public void create(Event event) {
		
		if ( event instanceof ClientCreatedEvent clientCreatedEvent) {
			
			clientRedisRepository.save(new ClientRedisEntity(clientCreatedEvent.getClientId()));
			logInfo(clientCreatedEvent.getClass().getSimpleName(), ApplicationConstant.CLIENTID + " : " + clientCreatedEvent.getClientId());

		}		
	}
	
	@Override
	public void deleteById(Event event) {
		
		if ( event instanceof ClientDeletedEvent clientDeletedEvent) {
			
			clientRedisRepository.deleteById(clientDeletedEvent.getClientId());
			logInfo(clientDeletedEvent.getClass().getSimpleName(), ApplicationConstant.CLIENTID + " : " + clientDeletedEvent.getClientId());

		}	
	}

	private void logInfo(String action, String entityInfo) {
		
	    loggerUtil.logInfoAction(this.getClass().getSimpleName(), action, entityInfo);	    
	
	}

	@Override
	public boolean existsById(UUID id) {
		
		logInfo("existById", ApplicationConstant.CLIENTID + " : " + id);
		return clientRedisRepository.existsById(id);
		
	}
	
}
