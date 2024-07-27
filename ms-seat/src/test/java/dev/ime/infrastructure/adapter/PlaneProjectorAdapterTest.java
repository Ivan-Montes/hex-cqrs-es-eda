package dev.ime.infrastructure.adapter;

import static org.mockito.Mockito.times;

import java.util.Optional;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.event.PlaneCreatedEvent;
import dev.ime.application.event.PlaneDeletedEvent;
import dev.ime.application.event.PlaneUpdatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.infrastructure.entity.PlaneMongoEntity;
import dev.ime.infrastructure.repository.read.PlaneNoSqlReadRepository;

@ExtendWith(MockitoExtension.class)
class PlaneProjectorAdapterTest {

	@Mock
	private LoggerUtil loggerUtil;
	
	@Mock
	private PlaneNoSqlReadRepository planeNoSqlReadRepository;
	
	@InjectMocks
	private PlaneProjectorAdapter planeProjectorAdapter;
	
	private PlaneMongoEntity planeMongoEntityTest;
	private final Long databaseSequence = 27L;
	private final ObjectId objectId = ObjectId.get();
	private final UUID planeId = UUID.randomUUID();
	private final String name = "Pursuit Special";
	private final Integer capacity = 2;
	

	@BeforeEach
	private void createObjects() {		
		
		planeMongoEntityTest = new PlaneMongoEntity(
				objectId,
				planeId,
				name,
				capacity
				);		
	}
	
	@Test
	void PlaneProjectorAdapter_create_ReturnVoid() {

		PlaneCreatedEvent event = new PlaneCreatedEvent(
				databaseSequence,
				planeId,
				name,
				capacity
				);
		Mockito.when(planeNoSqlReadRepository.save(Mockito.any(PlaneMongoEntity.class))).thenReturn(planeMongoEntityTest);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		planeProjectorAdapter.create(event);
		
		Mockito.verify(planeNoSqlReadRepository, times(1)).save(Mockito.any(PlaneMongoEntity.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}

	@Test
	void PlaneProjectorAdapter_update_ReturnVoid() {

		PlaneUpdatedEvent event = new PlaneUpdatedEvent(
				databaseSequence,
				planeId,
				name,
				capacity
				);
		Mockito.when(planeNoSqlReadRepository.findFirstByPlaneId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(planeMongoEntityTest));
		Mockito.when(planeNoSqlReadRepository.save(Mockito.any(PlaneMongoEntity.class))).thenReturn(planeMongoEntityTest);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		
		planeProjectorAdapter.update(event);
		
		Mockito.verify(planeNoSqlReadRepository, times(1)).findFirstByPlaneId(Mockito.any(UUID.class));
		Mockito.verify(planeNoSqlReadRepository, times(1)).save(Mockito.any(PlaneMongoEntity.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}

	@Test
	void PlaneProjectorAdapter_update_ReturnVoidByOptEmpty() {

		PlaneUpdatedEvent event = new PlaneUpdatedEvent(
				databaseSequence,
				planeId,
				name,
				capacity
				);
		Mockito.when(planeNoSqlReadRepository.findFirstByPlaneId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		
		planeProjectorAdapter.update(event);
		
		Mockito.verify(planeNoSqlReadRepository, times(1)).findFirstByPlaneId(Mockito.any(UUID.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}

	@Test
	void PlaneProjectorAdapter_deleteById_ReturnVoid() {
		
		PlaneDeletedEvent event = new PlaneDeletedEvent(
				databaseSequence,
				planeId
				);
		Mockito.when(planeNoSqlReadRepository.findFirstByPlaneId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(planeMongoEntityTest));
		Mockito.doNothing().when(planeNoSqlReadRepository).delete(Mockito.any(PlaneMongoEntity.class));
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		
		planeProjectorAdapter.deleteById(event);

		Mockito.verify(planeNoSqlReadRepository, times(1)).findFirstByPlaneId(Mockito.any(UUID.class));
		Mockito.verify(planeNoSqlReadRepository, times(1)).delete(Mockito.any(PlaneMongoEntity.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());		
	}

	@Test
	void PlaneProjectorAdapter_deleteById_ReturnVoidByOptEmpty() {
		
		PlaneDeletedEvent event = new PlaneDeletedEvent(
				databaseSequence,
				planeId
				);
		Mockito.when(planeNoSqlReadRepository.findFirstByPlaneId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		
		planeProjectorAdapter.deleteById(event);

		Mockito.verify(planeNoSqlReadRepository, times(1)).findFirstByPlaneId(Mockito.any(UUID.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());		
	}
	
	
	
	
}
