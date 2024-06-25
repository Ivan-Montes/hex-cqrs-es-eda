package dev.ime.infrastructure.adapter;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.ReservationCreatedEvent;
import dev.ime.application.event.ReservationDeletedEvent;
import dev.ime.application.event.ReservationUpdatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.ReservationRedisProjectorPort;
import dev.ime.infrastructure.entity.ReservationRedisEntity;
import dev.ime.infrastructure.repository.ReservationRedisRepository;

@Repository
public class ReservationRedisProjectorAdapter implements ReservationRedisProjectorPort {

	private final LoggerUtil loggerUtil;
	private final ReservationRedisRepository reservationRedisRepository;
	
	public ReservationRedisProjectorAdapter(LoggerUtil loggerUtil,
			ReservationRedisRepository reservationRedisRepository) {
		super();
		this.loggerUtil = loggerUtil;
		this.reservationRedisRepository = reservationRedisRepository;
	}
	
	@Override
	public void create(Event event) {
		
		if ( event instanceof ReservationCreatedEvent reservationCreatedEvent) {
			
			ReservationRedisEntity reservationRedisEntity = buildReservationRedisEntity(reservationCreatedEvent);
			saveReservationRedisEntity(reservationRedisEntity);
			logInfo(reservationCreatedEvent.getClass().getSimpleName(), reservationRedisEntity.toString());

		}
		
	}
	
	private void saveReservationRedisEntity(ReservationRedisEntity reservationRedisEntity) {
		
		reservationRedisRepository.save(reservationRedisEntity);
		
	}

	private ReservationRedisEntity buildReservationRedisEntity(ReservationCreatedEvent reservationCreatedEvent) {
		
		return new ReservationRedisEntity(
				reservationCreatedEvent.getReservationId(),
				reservationCreatedEvent.getClientId(),
				reservationCreatedEvent.getFlightId(),
				reservationCreatedEvent.getSeatIdsSet()
				);
		
	}

	private void logInfo(String action, String entityInfo) {
		
	    loggerUtil.logInfoAction(this.getClass().getSimpleName(), action, entityInfo);	   
	    
	}

	@Override
	public void update(Event event) {

		if ( event instanceof ReservationUpdatedEvent reservationUpdatedEvent) {
			
			UUID reservationId = reservationUpdatedEvent.getReservationId();
			Optional<ReservationRedisEntity> optReservationFound = findReservation(reservationId);
			
			if ( optReservationFound.isEmpty() ) {
				
				logInfo("ReservationUpdatedEvent] -> [" + ApplicationConstant.EX_RESOURCE_NOT_FOUND, ApplicationConstant.RESERVID + " : " + reservationId.toString() );
				return;			
				
			}
			
			ReservationRedisEntity reservationRedisEntityFound = optReservationFound.get();
			updateReservationRedisEntity(reservationUpdatedEvent, reservationRedisEntityFound);
			saveReservationRedisEntity(reservationRedisEntityFound);
			logInfo(reservationUpdatedEvent.getClass().getSimpleName(), reservationRedisEntityFound.toString());

		}	
		
	}
	
	private void updateReservationRedisEntity(ReservationUpdatedEvent reservationUpdatedEvent,
			ReservationRedisEntity reservationRedisEntityFound) {
		
		reservationRedisEntityFound.setClientId(reservationUpdatedEvent.getClientId());
		reservationRedisEntityFound.setFlightId(reservationUpdatedEvent.getFlightId());
		reservationRedisEntityFound.setSeatIdsSet(reservationUpdatedEvent.getSeatIdsSet());
		
	}

	private Optional<ReservationRedisEntity> findReservation(UUID reservationId) {
		
		return reservationRedisRepository.findById(reservationId);
		
	}

	@Override
	public void deleteById(Event event) {
		
		if ( event instanceof ReservationDeletedEvent reservationDeletedEvent) {
			
			UUID reservationId = reservationDeletedEvent.getReservationId();
			Optional<ReservationRedisEntity> optReservationFound = findReservation(reservationId);
			
			if ( optReservationFound.isEmpty() ) {
				
				logInfo("ReservationDeletedEvent] -> [" + ApplicationConstant.EX_RESOURCE_NOT_FOUND, ApplicationConstant.RESERVID + " : " + reservationId.toString() );
				return;			
				
			}
			
			reservationRedisRepository.delete(optReservationFound.get());
			logInfo(reservationDeletedEvent.getClass().getSimpleName(), ApplicationConstant.RESERVID + " : " + reservationId);
	
		}
	}
	
	@Override
	public boolean existsReservationRedisEntityByFlighId(UUID flightId) {
		
		logInfo("existsReservationRedisEntityByFlighId", ApplicationConstant.FLIGHTID + " : " + flightId);
		return !reservationRedisRepository.findByFlightId(flightId).isEmpty();
		
	}
	
}
