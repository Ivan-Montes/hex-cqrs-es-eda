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

import dev.ime.application.event.SeatCreatedEvent;
import dev.ime.application.event.SeatDeletedEvent;
import dev.ime.application.event.SeatUpdatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.infrastructure.entity.PlaneMongoEntity;
import dev.ime.infrastructure.entity.SeatMongoEntity;
import dev.ime.infrastructure.repository.read.PlaneNoSqlReadRepository;
import dev.ime.infrastructure.repository.read.SeatNoSqlReadRepository;

@ExtendWith(MockitoExtension.class)
class SeatProjectorAdapterTest {

	@Mock
	private LoggerUtil loggerUtil;	

	@Mock
	private SeatNoSqlReadRepository seatNoSqlReadRepository;

	@Mock
	private PlaneNoSqlReadRepository planeNoSqlReadRepository;

	@InjectMocks
	private SeatProjectorAdapter seatProjectorAdapter;

	private SeatMongoEntity seatMongoEntityTest;
	private PlaneMongoEntity planeMongoEntityTest;
	private final ObjectId objectId = ObjectId.get();
	private final Long databaseSequence = 27L;
	private final UUID seatId = UUID.randomUUID();
	private final String seatNumber = "PS1973";
	private final UUID planeId = UUID.randomUUID();
	private final String name = "Pursuit Special";
	private final Integer capacity = 2;
	
	@BeforeEach
	private void createObjects() {
		
		seatMongoEntityTest = new SeatMongoEntity(
				objectId,
				seatId,
				seatNumber,
				planeId
				);

		planeMongoEntityTest = new PlaneMongoEntity();
		planeMongoEntityTest.setPlaneId(planeId);
		planeMongoEntityTest.setName(name);
		planeMongoEntityTest.setCapacity(capacity);		
		
	}
	
	@Test
	void SeatProjectorAdapter_create_ReturnVoid() {
		
		SeatCreatedEvent event = new SeatCreatedEvent(
				databaseSequence,
				seatId,
				seatNumber,
				planeId
				);
		Mockito.when(planeNoSqlReadRepository.findFirstByPlaneId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(planeMongoEntityTest));
		Mockito.when(seatNoSqlReadRepository.save(Mockito.any(SeatMongoEntity.class))).thenReturn(seatMongoEntityTest);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		seatProjectorAdapter.create(event);
		
		Mockito.verify(planeNoSqlReadRepository, times(1)).findFirstByPlaneId(Mockito.any(UUID.class));
		Mockito.verify(seatNoSqlReadRepository, times(1)).save(Mockito.any(SeatMongoEntity.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}

	@Test
	void SeatProjectorAdapter_update_ReturnVoid() {
		
		SeatUpdatedEvent event = new SeatUpdatedEvent(
				databaseSequence,
				seatId,
				seatNumber,
				planeId
				);
		Mockito.when(seatNoSqlReadRepository.findFirstBySeatId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(seatMongoEntityTest));
		Mockito.when(planeNoSqlReadRepository.findFirstByPlaneId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(planeMongoEntityTest));
		Mockito.when(seatNoSqlReadRepository.save(Mockito.any(SeatMongoEntity.class))).thenReturn(seatMongoEntityTest);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		seatProjectorAdapter.update(event);

		Mockito.verify(seatNoSqlReadRepository, times(1)).findFirstBySeatId(Mockito.any(UUID.class));
		Mockito.verify(planeNoSqlReadRepository, times(1)).findFirstByPlaneId(Mockito.any(UUID.class));
		Mockito.verify(seatNoSqlReadRepository, times(1)).save(Mockito.any(SeatMongoEntity.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());		
	}

	@Test
	void SeatProjectorAdapter_update_ReturnVoidOptSeatEmpty() {
		
		SeatUpdatedEvent event = new SeatUpdatedEvent(
				databaseSequence,
				seatId,
				seatNumber,
				planeId
				);
		Mockito.when(seatNoSqlReadRepository.findFirstBySeatId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		seatProjectorAdapter.update(event);

		Mockito.verify(seatNoSqlReadRepository, times(1)).findFirstBySeatId(Mockito.any(UUID.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());		
	}	

	@Test
	void SeatProjectorAdapter_update_ReturnVoidOptPlaneEmpty() {
		
		SeatUpdatedEvent event = new SeatUpdatedEvent(
				databaseSequence,
				seatId,
				seatNumber,
				planeId
				);
		Mockito.when(seatNoSqlReadRepository.findFirstBySeatId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(seatMongoEntityTest));
		Mockito.when(planeNoSqlReadRepository.findFirstByPlaneId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		seatProjectorAdapter.update(event);

		Mockito.verify(seatNoSqlReadRepository, times(1)).findFirstBySeatId(Mockito.any(UUID.class));
		Mockito.verify(planeNoSqlReadRepository, times(1)).findFirstByPlaneId(Mockito.any(UUID.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());		
	}
	
	@Test
	void SeatProjectorAdapter_deleteById_ReturnVoid() {

		SeatDeletedEvent event = new SeatDeletedEvent(
				databaseSequence,
				seatId
				);
		Mockito.when(seatNoSqlReadRepository.findFirstBySeatId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(seatMongoEntityTest));
		Mockito.doNothing().when(seatNoSqlReadRepository).delete(Mockito.any(SeatMongoEntity.class));
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		
		seatProjectorAdapter.deleteById(event);

		Mockito.verify(seatNoSqlReadRepository, times(1)).findFirstBySeatId(Mockito.any(UUID.class));
		Mockito.verify(seatNoSqlReadRepository, times(1)).delete(Mockito.any(SeatMongoEntity.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());	
	}

	@Test
	void SeatProjectorAdapter_deleteById_ReturnVoidByOptEmpty() {

		SeatDeletedEvent event = new SeatDeletedEvent(
				databaseSequence,
				seatId
				);
		Mockito.when(seatNoSqlReadRepository.findFirstBySeatId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		
		seatProjectorAdapter.deleteById(event);

		Mockito.verify(seatNoSqlReadRepository, times(1)).findFirstBySeatId(Mockito.any(UUID.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());	
	}
	
}
