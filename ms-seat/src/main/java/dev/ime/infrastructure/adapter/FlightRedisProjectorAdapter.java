package dev.ime.infrastructure.adapter;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.FlightCreatedEvent;
import dev.ime.application.event.FlightDeletedEvent;
import dev.ime.application.event.FlightUpdatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.FlightRedisProjectorPort;
import dev.ime.infrastructure.entity.FlightRedisEntity;
import dev.ime.infrastructure.repository.FlightRedisRepository;

@Repository
public class FlightRedisProjectorAdapter implements FlightRedisProjectorPort{

	private final LoggerUtil loggerUtil;
	private final FlightRedisRepository flightRedisRepository;
	
	public FlightRedisProjectorAdapter(LoggerUtil loggerUtil, FlightRedisRepository flightRedisRepository) {
		super();
		this.loggerUtil = loggerUtil;
		this.flightRedisRepository = flightRedisRepository;
	}
	
	@Override
	public boolean existFlightRedisEntityByPlaneId(UUID planeId) {
		
		logInfo("existFlightRedisEntityByPlaneId", ApplicationConstant.PLANEID + " : " + planeId);
		return !flightRedisRepository.findByPlaneId(planeId).isEmpty();
		
	}
	
	@Override
	public void save(Event event) {
		
		if ( event instanceof FlightCreatedEvent flightCreatedEvent) {

			flightRedisRepository.save(
					new FlightRedisEntity(
						flightCreatedEvent.getFlightId(),
						flightCreatedEvent.getPlaneId()
					));
			
			logInfo(flightCreatedEvent.getClass().getSimpleName(), ApplicationConstant.FLIGHTID + " : " + flightCreatedEvent.getFlightId());
	
		}
	}

	@Override
	public void update(Event event) {
		
		if ( event instanceof FlightUpdatedEvent flightUpdatedEvent) {
			
			flightRedisRepository.save(
					new FlightRedisEntity(
							flightUpdatedEvent.getFlightId(),
							flightUpdatedEvent.getPlaneId()
					));
			
			logInfo(flightUpdatedEvent.getClass().getSimpleName(), ApplicationConstant.FLIGHTID + " : " + flightUpdatedEvent.getFlightId());
		
		}	
	}
	
	@Override
	public void deleteById(Event event) {
		
		if ( event instanceof FlightDeletedEvent flightDeletedEvent) {
			
			flightRedisRepository.deleteById(flightDeletedEvent.getFlightId());
			logInfo(flightDeletedEvent.getClass().getSimpleName(), ApplicationConstant.FLIGHTID + " : " + flightDeletedEvent.getFlightId());

		}
	}	

	private void logInfo(String action, String entityInfo) {
		
	    loggerUtil.logInfoAction(this.getClass().getSimpleName(), action, entityInfo);	    
	
	}	

}
