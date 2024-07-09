package dev.ime.infrastructure.adapter;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.ReservationCreatedEvent;
import dev.ime.application.event.ReservationDeletedEvent;
import dev.ime.application.event.ReservationUpdatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.BaseProjectorPort;
import dev.ime.domain.port.outbound.ExtendedProjectorPort;
import dev.ime.infrastructure.entity.ReservationJpaEntity;
import dev.ime.infrastructure.repository.ReservationJpaRepository;

@Repository
@Qualifier("reservationProjectorAdapter")
public class ReservationProjectorAdapter implements BaseProjectorPort, ExtendedProjectorPort{

	private final LoggerUtil loggerUtil;
	private final ReservationJpaRepository reservationJpaRepository;
	
	public ReservationProjectorAdapter(LoggerUtil loggerUtil, ReservationJpaRepository reservationJpaRepository) {
		super();
		this.loggerUtil = loggerUtil;
		this.reservationJpaRepository = reservationJpaRepository;
	}

	@Override
	public void create(Event event) {
		
		if ( event instanceof ReservationCreatedEvent reservationCreatedEvent) {
			
			ReservationJpaEntity reservationJpaEntity = buildReservationJpaEntity(reservationCreatedEvent);
			saveReservationJpaEntity(reservationJpaEntity);
			logInfo(reservationCreatedEvent.getClass().getSimpleName(), reservationJpaEntity.toString());

		}
	}

	@Override
	public void deleteById(Event event) {
		
		if ( event instanceof ReservationDeletedEvent reservationDeletedEvent) {
			
			UUID reservationId = reservationDeletedEvent.getReservationId();
			Optional<ReservationJpaEntity> optReservationFound = findReservation(reservationId);
			
			if ( optReservationFound.isEmpty() ) {
				
				logInfo("ReservationDeletedEvent] -> [" + ApplicationConstant.EX_RESOURCE_NOT_FOUND, ApplicationConstant.RESERVID + " : " + reservationId.toString() );
				return;			
				
			}
			
			reservationJpaRepository.delete(optReservationFound.get());
			logInfo(reservationDeletedEvent.getClass().getSimpleName(), ApplicationConstant.RESERVID + " : " + reservationId);
	
		}
	}

	@Override
	public void update(Event event) {
		
		if ( event instanceof ReservationUpdatedEvent reservationUpdatedEvent) {
			
			UUID reservationId = reservationUpdatedEvent.getReservationId();
			Optional<ReservationJpaEntity> optReservationFound = findReservation(reservationId);
			
			if ( optReservationFound.isEmpty() ) {
				
				logInfo("ReservationUpdatedEvent] -> [" + ApplicationConstant.EX_RESOURCE_NOT_FOUND, ApplicationConstant.RESERVID + " : " + reservationId.toString() );
				return;			
				
			}
			
			ReservationJpaEntity reservationJpaEntityFound = optReservationFound.get();
			updateReservationJpaEntity(reservationUpdatedEvent, reservationJpaEntityFound);
			saveReservationJpaEntity(reservationJpaEntityFound);
			logInfo(reservationUpdatedEvent.getClass().getSimpleName(), reservationJpaEntityFound.toString());

		}		
	}


	private void updateReservationJpaEntity(ReservationUpdatedEvent reservationUpdatedEvent,
			ReservationJpaEntity reservationJpaEntityFound) {
		
		reservationJpaEntityFound.setClientId(reservationUpdatedEvent.getClientId());
		reservationJpaEntityFound.setFlightId(reservationUpdatedEvent.getFlightId());
		reservationJpaEntityFound.setSeatIdsSet(reservationUpdatedEvent.getSeatIdsSet());
		
	}

	private ReservationJpaEntity buildReservationJpaEntity(ReservationCreatedEvent reservationCreatedEvent) {
		
		ReservationJpaEntity reservationJpaEntity = new ReservationJpaEntity();
		reservationJpaEntity.setReservationId(reservationCreatedEvent.getReservationId());
		reservationJpaEntity.setClientId(reservationCreatedEvent.getClientId());
		reservationJpaEntity.setFlightId(reservationCreatedEvent.getFlightId());
		reservationJpaEntity.setSeatIdsSet(reservationCreatedEvent.getSeatIdsSet());
		
		return reservationJpaEntity;
		
	}
	
	private void saveReservationJpaEntity(ReservationJpaEntity reservationJpaEntity) {
		
		reservationJpaRepository.save(reservationJpaEntity);
		
	}

	private Optional<ReservationJpaEntity> findReservation(UUID reservationId) {
		
		return reservationJpaRepository.findById(reservationId);
		
	}

	private void logInfo(String action, String entityInfo) {
		
	    loggerUtil.logInfoAction(this.getClass().getSimpleName(), action, entityInfo);
	    
	}

	@Override
	public boolean existsById(UUID id) {
		
		logInfo("existById", ApplicationConstant.RESERVID + " : " + id);
		return reservationJpaRepository.existsById(id);
		
	}
	
}
