package dev.ime.infrastructure.adapter;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.SeatCreatedEvent;
import dev.ime.application.event.SeatDeletedEvent;
import dev.ime.application.event.SeatUpdatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.BaseProjectorPort;
import dev.ime.domain.port.outbound.ExtendedProjectorPort;
import dev.ime.domain.port.outbound.SeatSpecificProjectorPort;
import dev.ime.infrastructure.entity.SeatRedisEntity;
import dev.ime.infrastructure.repository.SeatRedisRepository;

@Repository
@Qualifier("seatRedisProjectorAdapter")
public class SeatRedisProjectorAdapter implements BaseProjectorPort, ExtendedProjectorPort, SeatSpecificProjectorPort{

	private final LoggerUtil loggerUtil;
	private final SeatRedisRepository seatRedisRepository;
	
	public SeatRedisProjectorAdapter(LoggerUtil loggerUtil, SeatRedisRepository seatRedisRepository) {
		super();
		this.loggerUtil = loggerUtil;
		this.seatRedisRepository = seatRedisRepository;
	}
	
	@Override
	public void update(Event event) {
		
		if ( event instanceof SeatUpdatedEvent seatUpdatedEvent) {

			seatRedisRepository.save(
					new SeatRedisEntity(
							seatUpdatedEvent.getSeatId(),
							seatUpdatedEvent.getPlaneId()
					));
			
			logInfo(seatUpdatedEvent.getClass().getSimpleName(), ApplicationConstant.SEATID + " : " + seatUpdatedEvent.getSeatId());
	
		}	
	}
	
	@Override
	public void create(Event event) {
		
		if ( event instanceof SeatCreatedEvent seatCreatedEvent) {

			seatRedisRepository.save(
					new SeatRedisEntity(
							seatCreatedEvent.getSeatId(),
							seatCreatedEvent.getPlaneId()
					));
			
			logInfo(seatCreatedEvent.getClass().getSimpleName(), ApplicationConstant.SEATID + " : " + seatCreatedEvent.getSeatId());
	
		}		
	}
	
	@Override
	public void deleteById(Event event) {
		
		if ( event instanceof SeatDeletedEvent seatDeletedEvent) {

			seatRedisRepository.deleteById(seatDeletedEvent.getSeatId());			
			logInfo(seatDeletedEvent.getClass().getSimpleName(), ApplicationConstant.SEATID + " : " + seatDeletedEvent.getSeatId());
	
		}
	}

	private void logInfo(String action, String entityInfo) {
		
	    loggerUtil.logInfoAction(this.getClass().getSimpleName(), action, entityInfo);	    
	
	}

	@Override
	public boolean existsById(UUID id) {
		
		logInfo("existById", ApplicationConstant.SEATID + " : " + id);
		return seatRedisRepository.existsById(id);
		
	}

	@Override
	public List<UUID> findSeatByPlaneId(UUID id) {
		
		return seatRedisRepository.findByPlaneId(id)
				.stream()
				.map( sre -> sre.getSeatId())
				.toList();
		
	}	

}
