package dev.ime.infrastructure.adapter;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.PlaneCreatedEvent;
import dev.ime.application.event.PlaneDeletedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.PlaneRedisProjectorPort;
import dev.ime.infrastructure.entity.PlaneRedisEntity;
import dev.ime.infrastructure.repository.PlaneRedisRepository;

@Repository
public class PlaneRedisProjectorAdapter implements PlaneRedisProjectorPort{

	private final LoggerUtil loggerUtil;
	private final PlaneRedisRepository planeRedisRepository;

	public PlaneRedisProjectorAdapter(LoggerUtil loggerUtil, PlaneRedisRepository planeRedisRepository) {
		super();
		this.loggerUtil = loggerUtil;
		this.planeRedisRepository = planeRedisRepository;
	}

	@Override
	public boolean existById(UUID id) {	
		
		logInfo("existById", ApplicationConstant.PLANEID + " : " + id);
		return planeRedisRepository.existsById(id);
		
	}

	@Override
	public void save(Event event) {
		
		if ( event instanceof PlaneCreatedEvent planeCreatedEvent) {
			
			planeRedisRepository.save(new PlaneRedisEntity(planeCreatedEvent.getPlaneId()));			
			logInfo(planeCreatedEvent.getClass().getSimpleName(), ApplicationConstant.PLANEID + " : " + planeCreatedEvent.getPlaneId());

		}			
	}

	@Override
	public void deleteById(Event event) {
		
		if ( event instanceof PlaneDeletedEvent planeDeletedEvent){
		
			planeRedisRepository.delete(new PlaneRedisEntity(planeDeletedEvent.getPlaneId()));
			logInfo(planeDeletedEvent.getClass().getSimpleName(), ApplicationConstant.PLANEID + " : " + planeDeletedEvent.getPlaneId());

		}		
	}	

	private void logInfo(String action, String entityInfo) {
		
	    loggerUtil.logInfoAction(this.getClass().getSimpleName(), action, entityInfo);	    
	
	}
	
}
