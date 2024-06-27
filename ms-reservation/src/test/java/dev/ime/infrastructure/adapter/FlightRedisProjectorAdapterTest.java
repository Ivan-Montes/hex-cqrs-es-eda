package dev.ime.infrastructure.adapter;

import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
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
import dev.ime.infrastructure.entity.FlightRedisEntity;
import dev.ime.infrastructure.repository.FlightRedisRepository;

@ExtendWith(MockitoExtension.class)
class FlightRedisProjectorAdapterTest {

	@Mock
	private LoggerUtil loggerUtil;
	
	@Mock
	private  FlightRedisRepository flightRedisRepository;

	@InjectMocks
	private FlightRedisProjectorAdapter flightRedisProjectorPort;
	
	private final Long databaseSequence = 37L;
	private final UUID flightId = UUID.randomUUID();
	private final String origin = "Madrid";
	private final String destiny = "Tokyo";
	private final LocalDate departureDate = LocalDate.parse("2024-04-01");
	private final LocalTime departureTime = LocalTime.parse("15:00");
	private final UUID planeId = UUID.randomUUID();
	private FlightRedisEntity flightRedisEntityTest;
	
	@BeforeEach
	private void createObjects() {
		
		flightRedisEntityTest = new FlightRedisEntity();
		flightRedisEntityTest.setFlightId(flightId);
		flightRedisEntityTest.setPlaneId(planeId);
		
	}	

	@Test
	void FlightRedisProjectorAdapter_save_ReturnVoid() {
		
		FlightCreatedEvent event = new FlightCreatedEvent(
				databaseSequence,
				flightId,
				origin,
				destiny,
				departureDate,
				departureTime,
				planeId);
		Mockito.when(flightRedisRepository.save(Mockito.any(FlightRedisEntity.class))).thenReturn(flightRedisEntityTest);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		
		flightRedisProjectorPort.create(event);
		
		Mockito.verify(flightRedisRepository, times(1)).save(Mockito.any(FlightRedisEntity.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}

	@Test
	void FlightRedisProjectorAdapter_update_ReturnVoid() {
		
		FlightUpdatedEvent event = new FlightUpdatedEvent(
				databaseSequence,
				flightId,
				origin,
				destiny,
				departureDate,
				departureTime,
				planeId);
		Mockito.when(flightRedisRepository.save(Mockito.any(FlightRedisEntity.class))).thenReturn(flightRedisEntityTest);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		
		flightRedisProjectorPort.update(event);
		
		Mockito.verify(flightRedisRepository, times(1)).save(Mockito.any(FlightRedisEntity.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}

	@Test
	void FlightRedisProjectorAdapter_deleteById_ReturnVoid() {
		
		FlightDeletedEvent event = new FlightDeletedEvent(
				databaseSequence,
				flightId
		);
		Mockito.doNothing().when(flightRedisRepository).deleteById(Mockito.any(UUID.class));
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		
		flightRedisProjectorPort.deleteById(event);
		
		Mockito.verify(flightRedisRepository, times(1)).deleteById(Mockito.any(UUID.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());	
	}

	@Test
	void FlightRedisProjectorAdapter_existById_ReturnBooleanFalse() {
		
		Mockito.when(flightRedisRepository.existsById(Mockito.any(UUID.class))).thenReturn(false);
		
		boolean resultValue = flightRedisProjectorPort.existsById(UUID.randomUUID());
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isFalse()
				);	
	}

	@Test
	void FlightRedisProjectorAdapter_findPlaneRegardingFlight_ReturnUUID() {
		
		Mockito.when(flightRedisRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(flightRedisEntityTest));
		
		UUID planeIdFound = flightRedisProjectorPort.findPlaneRegardingFlight(UUID.randomUUID());
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(planeIdFound).isNotNull(),
				()-> Assertions.assertThat(planeIdFound).isEqualTo(planeId)
				);	
	}
	
}
