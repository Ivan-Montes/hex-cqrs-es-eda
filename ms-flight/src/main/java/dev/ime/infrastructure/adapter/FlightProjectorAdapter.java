package dev.ime.infrastructure.adapter;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.FlightCreatedEvent;
import dev.ime.application.event.FlightDeletedEvent;
import dev.ime.application.event.FlightUpdatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.FlightProjectorPort;
import dev.ime.infrastructure.entity.FlightMongoEntity;
import dev.ime.infrastructure.repository.read.FlightNoSqlReadRepository;

@Repository
public class FlightProjectorAdapter implements FlightProjectorPort{

	private final FlightNoSqlReadRepository flightNoSqlReadRepository;
	private final LoggerUtil loggerUtil;	
	
	public FlightProjectorAdapter(FlightNoSqlReadRepository flightNoSqlReadRepository, LoggerUtil loggerUtil) {
		super();
		this.flightNoSqlReadRepository = flightNoSqlReadRepository;
		this.loggerUtil = loggerUtil;
	}

	@Override
	public void create(Event event) {
		
		if ( event instanceof FlightCreatedEvent flightCreatedEvent) {
			
			FlightMongoEntity flightMongoEntity = buildFlightMongoEntity(flightCreatedEvent);			
			saveFlightEntity(flightMongoEntity);			
			logInfo("FlightCreatedEvent", flightMongoEntity.toString());
			
		}		
	}

	@Override
	public void update(Event event) {
		
		if ( event instanceof FlightUpdatedEvent flightUpdatedEvent) {
			
			UUID flightId = flightUpdatedEvent.getFlightId();
			Optional<FlightMongoEntity> optFlightFound = flightNoSqlReadRepository.findFirstByFlightId(flightId);
	
			if ( optFlightFound.isEmpty() ) {
				
				logInfo("FlightUpdatedEvent] -> [ResourceNotFoundException", ApplicationConstant.FLIGHTID + " : " + flightId );
				return;			
				
			}
		
			FlightMongoEntity flightMongoEntityFound = optFlightFound.get();
			updateFlightMongoEntity(flightUpdatedEvent, flightMongoEntityFound);		
			saveFlightEntity(flightMongoEntityFound);
			logInfo("FlightUpdatedEvent", flightMongoEntityFound.toString());

		}		
	}

	private void updateFlightMongoEntity(FlightUpdatedEvent flightUpdatedEvent,
			FlightMongoEntity flightMongoEntityFound) {
		
		flightMongoEntityFound.setOrigin(flightUpdatedEvent.getOrigin());
		flightMongoEntityFound.setDestiny(flightUpdatedEvent.getDestiny());
		flightMongoEntityFound.setDepartureDate(flightUpdatedEvent.getDepartureDate());
		flightMongoEntityFound.setDepartureTime(flightUpdatedEvent.getDepartureTime());
		flightMongoEntityFound.setPlaneId(flightUpdatedEvent.getPlaneId());
		
	}

	@Override
	public void deleteById(Event event) {
		
		if ( event instanceof FlightDeletedEvent flightDeletedEvent) {
			
			UUID flightId = flightDeletedEvent.getFlightId();
			Optional<FlightMongoEntity> optFlightFound = flightNoSqlReadRepository.findFirstByFlightId(flightId);
	
			if ( optFlightFound.isEmpty() ) {
				
				logInfo("FlightDeletedEvent] -> [ResourceNotFoundException", ApplicationConstant.FLIGHTID + " : " + flightId );
				return;			
				
			}
			
			flightNoSqlReadRepository.delete(optFlightFound.get());
			logInfo("FlightDeletedEvent", ApplicationConstant.FLIGHTID + " : " + flightId);
		
		}
	}
	
	private FlightMongoEntity buildFlightMongoEntity(FlightCreatedEvent flightCreatedEvent) {
		
		FlightMongoEntity flightMongoEntity = new FlightMongoEntity();
		flightMongoEntity.setFlightId(flightCreatedEvent.getFlightId());
		flightMongoEntity.setOrigin(flightCreatedEvent.getOrigin());
		flightMongoEntity.setDestiny(flightCreatedEvent.getDestiny());
		flightMongoEntity.setDepartureDate(flightCreatedEvent.getDepartureDate());
		flightMongoEntity.setDepartureTime(flightCreatedEvent.getDepartureTime());
		flightMongoEntity.setPlaneId(flightCreatedEvent.getPlaneId());
		
		return flightMongoEntity;
		
	}
	
	private void saveFlightEntity(FlightMongoEntity flightMongoEntity) {
		
		flightNoSqlReadRepository.save(flightMongoEntity);
		
	}
	
	private void logInfo(String action, String entityInfo) {
		
	    loggerUtil.logInfoAction(this.getClass().getSimpleName(), action, entityInfo);	    
	
	}
	
}
