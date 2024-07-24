package dev.ime.infrastructure.adapter;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.SeatCreatedEvent;
import dev.ime.application.event.SeatDeletedEvent;
import dev.ime.application.event.SeatUpdatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.GenericProjectorPort;
import dev.ime.infrastructure.entity.PlaneMongoEntity;
import dev.ime.infrastructure.entity.SeatMongoEntity;
import dev.ime.infrastructure.repository.read.PlaneNoSqlReadRepository;
import dev.ime.infrastructure.repository.read.SeatNoSqlReadRepository;


@Repository
@Qualifier("seatProjectorAdapter")
public class SeatProjectorAdapter implements GenericProjectorPort{

	private final LoggerUtil loggerUtil;	
	private final SeatNoSqlReadRepository seatNoSqlReadRepository;
	private final PlaneNoSqlReadRepository planeNoSqlReadRepository;
	
	public SeatProjectorAdapter(LoggerUtil loggerUtil, SeatNoSqlReadRepository seatNoSqlReadRepository,
			PlaneNoSqlReadRepository planeNoSqlReadRepository) {
		super();
		this.loggerUtil = loggerUtil;
		this.seatNoSqlReadRepository = seatNoSqlReadRepository;
		this.planeNoSqlReadRepository = planeNoSqlReadRepository;
	}

	@Override
	public void create(Event event) {
		
		if ( event instanceof SeatCreatedEvent seatCreatedEvent) {
			
			UUID planeId = seatCreatedEvent.getPlaneId();
			Optional<PlaneMongoEntity> optPlaneFound = findPlane(planeId);
			
			if ( optPlaneFound.isEmpty() ) {
				
				logInfo("SeatCreatedEvent] -> [" + ApplicationConstant.EX_RESOURCE_NOT_FOUND, ApplicationConstant.PLANEID + " : " + planeId );
				return;			
				
			}	
			
			SeatMongoEntity seatMongoEntity = buildSeatMongoEntity(seatCreatedEvent, optPlaneFound.get());			
			saveSeatEntity(seatMongoEntity);			
			logInfo(seatCreatedEvent.getClass().getSimpleName(), seatMongoEntity.toString());
			
		}		
	}	

	@Override
	public void update(Event event) {
		
		if ( event instanceof SeatUpdatedEvent seatUpdatedEvent) {
			
			UUID seatId = seatUpdatedEvent.getSeatId();
			Optional<SeatMongoEntity> optSeatFound = findSeat(seatId);
			
			if ( optSeatFound.isEmpty() ) {
				
				logInfo("SeatUpdatedEvent] -> [" + ApplicationConstant.EX_RESOURCE_NOT_FOUND, ApplicationConstant.SEATID + " : " + seatId );
				return;			
				
			}
			
			UUID planeId = seatUpdatedEvent.getPlaneId();
			Optional<PlaneMongoEntity> optPlaneFound = findPlane(planeId);
			
			if ( optPlaneFound.isEmpty() ) {
				
				logInfo("SeatUpdatedEvent] -> [" + ApplicationConstant.EX_RESOURCE_NOT_FOUND, ApplicationConstant.PLANEID + " : " + planeId );
				return;			
				
			}	
			
			SeatMongoEntity seatMongoEntityFound = optSeatFound.get();
			PlaneMongoEntity planeMongoEntityFound = optPlaneFound.get();
			updateSeatMongoEntity(seatUpdatedEvent, seatMongoEntityFound, planeMongoEntityFound);
			saveSeatEntity(seatMongoEntityFound);			
			logInfo(seatUpdatedEvent.getClass().getSimpleName(), seatMongoEntityFound.toString());
		
		}		
	}

	@Override
	public void deleteById(Event event) {

		if ( event instanceof SeatDeletedEvent seatDeletedEvent) {
			
			UUID seatId = seatDeletedEvent.getSeatId();
			Optional<SeatMongoEntity> optSeatFound = findSeat(seatId);
			
			if ( optSeatFound.isEmpty() ) {
				
				logInfo("SeatDeletedEvent] -> [" + ApplicationConstant.EX_RESOURCE_NOT_FOUND, ApplicationConstant.SEATID + " : " + seatId );
				return;			
				
			}
			
			seatNoSqlReadRepository.delete(optSeatFound.get());
			logInfo(seatDeletedEvent.getClass().getSimpleName(), ApplicationConstant.SEATID + " : " + seatId);
		
		}		
	}
	
	private Optional<PlaneMongoEntity> findPlane(UUID planeId){
		
		return planeNoSqlReadRepository.findFirstByPlaneId(planeId);
	
	}
	
	private Optional<SeatMongoEntity> findSeat(UUID seatId){
		
		return seatNoSqlReadRepository.findFirstBySeatId(seatId);
	
	}
	
	private SeatMongoEntity buildSeatMongoEntity(SeatCreatedEvent event, PlaneMongoEntity planeMongoEntityFound) {
		
		SeatMongoEntity seatMongoEntity = new SeatMongoEntity();
		seatMongoEntity.setSeatId(event.getSeatId());
		seatMongoEntity.setSeatNumber(event.getSeatNumber());
		seatMongoEntity.setPlaneId(planeMongoEntityFound.getPlaneId());
		
		return seatMongoEntity;
	
	}
	
	private void updateSeatMongoEntity(SeatUpdatedEvent event, SeatMongoEntity seatMongoFound, PlaneMongoEntity planeMongoEntityFound) {
		
		seatMongoFound.setSeatNumber(event.getSeatNumber());
		seatMongoFound.setPlaneId(planeMongoEntityFound.getPlaneId());
	
	}
	
	private void saveSeatEntity(SeatMongoEntity seatMongoEntity) {
		
		seatNoSqlReadRepository.save(seatMongoEntity);
		
	}
	
	private void logInfo(String action, String entityInfo) {
		
	    loggerUtil.logInfoAction(this.getClass().getSimpleName(), action, entityInfo);	    
	
	}
	
}
