package dev.ime.infrastructure.adapter;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.PlaneCreatedEvent;
import dev.ime.application.event.PlaneDeletedEvent;
import dev.ime.application.event.PlaneUpdatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.GenericProjectorPort;
import dev.ime.infrastructure.entity.PlaneMongoEntity;
import dev.ime.infrastructure.repository.read.PlaneNoSqlReadRepository;


@Repository
@Qualifier("planeProjectorAdapter")
public class PlaneProjectorAdapter implements GenericProjectorPort {

	private final LoggerUtil loggerUtil;
	private final PlaneNoSqlReadRepository planeNoSqlReadRepository;
	
	public PlaneProjectorAdapter(LoggerUtil loggerUtil, PlaneNoSqlReadRepository planeNoSqlReadRepository) {
		super();
		this.loggerUtil = loggerUtil;
		this.planeNoSqlReadRepository = planeNoSqlReadRepository;
	}

	@Override
	public void create(Event event) {
		
		if ( event instanceof PlaneCreatedEvent planeCreatedEvent){
			
			PlaneMongoEntity planeMongoEntity = buildPlaneMongoEntity(planeCreatedEvent);			
			savePlaneEntity(planeMongoEntity);			
			logInfo(planeCreatedEvent.getClass().getSimpleName(), planeMongoEntity.toString());
			
		}		
	}

	@Override
	public void update(Event event) {
		
		if ( event instanceof PlaneUpdatedEvent planeUpdatedEvent){
			
			UUID planeId = planeUpdatedEvent.getPlaneId();
			Optional<PlaneMongoEntity> optPlaneFound = findPlane(planeId);
			
			if ( optPlaneFound.isEmpty() ) {
				
				logInfo("PlaneUpdatedEvent] -> [" + ApplicationConstant.EX_RESOURCE_NOT_FOUND, ApplicationConstant.PLANEID + " : " + planeId );
				return;			
				
			}
			
			PlaneMongoEntity planeMongoEntityFound = optPlaneFound.get();
			updatePlaneMongoEntity(planeUpdatedEvent, planeMongoEntityFound);
			savePlaneEntity(planeMongoEntityFound);
			logInfo(planeUpdatedEvent.getClass().getSimpleName(), planeMongoEntityFound.toString());

		}		
	}

	@Override
	public void deleteById(Event event) {		

		if ( event instanceof PlaneDeletedEvent planeDeletedEvent){
			
			UUID planeId = planeDeletedEvent.getPlaneId();
			Optional<PlaneMongoEntity> optPlaneFound = findPlane(planeId);
			
			if ( optPlaneFound.isEmpty() ) {
				
				logInfo("PlaneUpdatedEvent] -> [" + ApplicationConstant.EX_RESOURCE_NOT_FOUND, ApplicationConstant.PLANEID + " : " + planeId );
				return;			
				
			}
			
			planeNoSqlReadRepository.delete(optPlaneFound.get());
			logInfo(planeDeletedEvent.getClass().getSimpleName(), ApplicationConstant.PLANEID + " : " + planeId);

		}				
	}
	
	private Optional<PlaneMongoEntity> findPlane(UUID planeId){
		
		return planeNoSqlReadRepository.findFirstByPlaneId(planeId);
		
	}
	
	private PlaneMongoEntity buildPlaneMongoEntity(PlaneCreatedEvent event) {
		
		PlaneMongoEntity planeMongoEntity = new PlaneMongoEntity();
		planeMongoEntity.setPlaneId(event.getPlaneId());
		planeMongoEntity.setName(event.getName());
		planeMongoEntity.setCapacity(event.getCapacity());
		
		return planeMongoEntity;
		
	}
	
	private void updatePlaneMongoEntity(PlaneUpdatedEvent event, PlaneMongoEntity planeMongoEntity) {
		
		planeMongoEntity.setName(event.getName());
		planeMongoEntity.setCapacity(event.getCapacity());
		
	}
	
	private void savePlaneEntity(PlaneMongoEntity planeMongoEntity) {
		
		planeNoSqlReadRepository.save(planeMongoEntity);
		
	}

	private void logInfo(String action, String entityInfo) {
		
	    loggerUtil.logInfoAction(this.getClass().getSimpleName(), action, entityInfo);	    
	
	}
	
}
