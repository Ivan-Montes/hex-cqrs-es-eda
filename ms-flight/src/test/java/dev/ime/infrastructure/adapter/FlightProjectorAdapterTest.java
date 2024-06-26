package dev.ime.infrastructure.adapter;

import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.time.LocalTime;
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

import dev.ime.application.event.FlightCreatedEvent;
import dev.ime.application.event.FlightDeletedEvent;
import dev.ime.application.event.FlightUpdatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.infrastructure.entity.FlightMongoEntity;
import dev.ime.infrastructure.repository.read.FlightNoSqlReadRepository;

@ExtendWith(MockitoExtension.class)
class FlightProjectorAdapterTest {

	@Mock
	private FlightNoSqlReadRepository flightNoSqlReadRepository;
	
	@Mock
	private LoggerUtil loggerUtil;
	
	@InjectMocks
	private FlightProjectorAdapter flightProjectorAdapter;		

	private FlightMongoEntity flightMongoEntityTest;
	private final Long databaseSequence = 11L;
	private final ObjectId objectId = ObjectId.get();
	private final UUID flightId = UUID.randomUUID();
	private final String origin = "Madrid";
	private final String destiny = "Tokyo";
	private final LocalDate departureDate = LocalDate.parse("2024-04-01");
	private final LocalTime departureTime = LocalTime.parse("15:00");
	private final UUID planeId = UUID.randomUUID();
	
	@BeforeEach
	private void createObjects() {

		flightMongoEntityTest = new FlightMongoEntity();
		flightMongoEntityTest.setMongoId(objectId);
		flightMongoEntityTest.setFlightId(flightId);
		flightMongoEntityTest.setOrigin(origin);
		flightMongoEntityTest.setDestiny(destiny);
		flightMongoEntityTest.setDepartureDate(departureDate);
		flightMongoEntityTest.setDepartureTime(departureTime);
		flightMongoEntityTest.setPlaneId(planeId);
		
	}
	
	@Test
	void FlightProjectorAdapter_create_ReturnVoid() {		

		FlightCreatedEvent event = new FlightCreatedEvent(
				databaseSequence,
				flightId,
				origin,
				destiny,
				departureDate,
				departureTime,
				planeId);
		Mockito.when(flightNoSqlReadRepository.save(Mockito.any(FlightMongoEntity.class))).thenReturn(flightMongoEntityTest);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		flightProjectorAdapter.create(event);
		
		Mockito.verify(flightNoSqlReadRepository, times(1)).save(Mockito.any(FlightMongoEntity.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}

	@Test
	void FlightProjectorAdapter_update_ReturnVoid() {
		
		FlightUpdatedEvent event = new FlightUpdatedEvent(
				databaseSequence,
				flightId,
				origin,
				destiny,
				departureDate,
				departureTime,
				planeId
				);
		Mockito.when(flightNoSqlReadRepository.findFirstByFlightId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(flightMongoEntityTest));
		Mockito.when(flightNoSqlReadRepository.save(Mockito.any(FlightMongoEntity.class))).thenReturn(flightMongoEntityTest);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		flightProjectorAdapter.update(event);
		
		Mockito.verify(flightNoSqlReadRepository, times(1)).findFirstByFlightId(Mockito.any(UUID.class));
		Mockito.verify(flightNoSqlReadRepository, times(1)).save(Mockito.any(FlightMongoEntity.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());		
	}

	@Test
	void FlightProjectorAdapter_update_ReturnVoidByOptEmpty() {
		
		FlightUpdatedEvent event = new FlightUpdatedEvent(
				databaseSequence,
				flightId,
				origin,
				destiny,
				departureDate,
				departureTime,
				planeId
				);
		Mockito.when(flightNoSqlReadRepository.findFirstByFlightId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		
		flightProjectorAdapter.update(event);
		
		Mockito.verify(flightNoSqlReadRepository, times(1)).findFirstByFlightId(Mockito.any(UUID.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());		
	}

	@Test
	void FlightProjectorAdapter_deleteById_ReturnVoid() {
		
		FlightDeletedEvent event = new FlightDeletedEvent(
				databaseSequence,
				flightId
		);
		Mockito.when(flightNoSqlReadRepository.findFirstByFlightId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(flightMongoEntityTest));
		Mockito.doNothing().when(flightNoSqlReadRepository).delete(Mockito.any(FlightMongoEntity.class));
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		flightProjectorAdapter.deleteById(event);
		
		Mockito.verify(flightNoSqlReadRepository, times(1)).findFirstByFlightId(Mockito.any(UUID.class));
		Mockito.verify(flightNoSqlReadRepository, times(1)).delete(Mockito.any(FlightMongoEntity.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());		
	}

	@Test
	void FlightProjectorAdapter_deleteById_ReturnVoidByOptEmpty() {
		
		FlightDeletedEvent event = new FlightDeletedEvent(
				databaseSequence,
				flightId
		);
		Mockito.when(flightNoSqlReadRepository.findFirstByFlightId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		flightProjectorAdapter.deleteById(event);
		
		Mockito.verify(flightNoSqlReadRepository, times(1)).findFirstByFlightId(Mockito.any(UUID.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());		
	}
}
