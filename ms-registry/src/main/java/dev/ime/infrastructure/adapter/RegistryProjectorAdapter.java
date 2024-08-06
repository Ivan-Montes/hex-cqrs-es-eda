package dev.ime.infrastructure.adapter;



import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.ime.application.event.RegistryCreatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.BaseProjectorPort;
import dev.ime.infrastructure.entity.RegistryJpaEntity;
import dev.ime.infrastructure.repository.RegistryJpaRepository;

@Repository
public class RegistryProjectorAdapter  implements BaseProjectorPort{

	private final LoggerUtil loggerUtil;
	private final RegistryJpaRepository registryJpaRepository;
	private final ObjectMapper objectMapper;
	
	public RegistryProjectorAdapter(LoggerUtil loggerUtil, RegistryJpaRepository registryJpaRepository,
			ObjectMapper objectMapper) {
		super();
		this.loggerUtil = loggerUtil;
		this.registryJpaRepository = registryJpaRepository;
		this.objectMapper = objectMapper;
	}

	@Override
	public void create(Event event) {
		
		if ( event instanceof RegistryCreatedEvent registryCreatedEvent) {
			
			RegistryJpaEntity registryJpaEntity = buildRegistryJpaEntity(registryCreatedEvent);
			saveRegistryJpaEntity(registryJpaEntity);
			logInfo(registryCreatedEvent.getClass().getSimpleName(), registryJpaEntity.toString());

		}

	}

	private void saveRegistryJpaEntity(RegistryJpaEntity registryJpaEntity) {
		
		registryJpaRepository.save(registryJpaEntity);
		
	}

	private RegistryJpaEntity buildRegistryJpaEntity(RegistryCreatedEvent registryCreatedEvent) {
		
		RegistryJpaEntity registryJpaEntity = new RegistryJpaEntity();
		registryJpaEntity.setEventId(registryCreatedEvent.getEventId());
		registryJpaEntity.setEventCategory(registryCreatedEvent.getEventCategory());
		registryJpaEntity.setEventType(registryCreatedEvent.getEventType());
		registryJpaEntity.setTimeInstant(registryCreatedEvent.getTimeInstant());
		registryJpaEntity.setSequence(registryCreatedEvent.getSequence());
		
		String eventDataString = "";
		try {
			
			eventDataString = objectMapper.writeValueAsString(registryCreatedEvent.getEventData());
		
		} catch (JsonProcessingException e) {

			logInfo(registryCreatedEvent.getClass().getSimpleName(), e.getLocalizedMessage());
			
		}	
		
		registryJpaEntity.setEventData(eventDataString);
		
		return registryJpaEntity;
	}

	private void logInfo(String action, String entityInfo) {
		
	    loggerUtil.logInfoAction(this.getClass().getSimpleName(), action, entityInfo);
	    
	}
	
}
