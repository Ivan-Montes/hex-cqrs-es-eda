package dev.ime.infrastructure.adapter;

import static org.mockito.Mockito.times;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.event.PlaneCreatedEvent;
import dev.ime.application.event.PlaneDeletedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.infrastructure.entity.PlaneRedisEntity;
import dev.ime.infrastructure.repository.PlaneRedisRepository;

@ExtendWith(MockitoExtension.class)
class PlaneRedisProjectorAdapterTest {

	@Mock
	private LoggerUtil loggerUtil;
	
	@Mock
	private PlaneRedisRepository planeRedisRepository;

	@InjectMocks
	private PlaneRedisProjectorAdapter planeRedisProjectorAdapter;

	private final Long databaseSequence = 27L;
	private final UUID planeId = UUID.randomUUID();
	private final String name = "Pursuit Special";
	private final Integer capacity = 2;
	private PlaneRedisEntity planeRedisEntity;
	
	@BeforeEach
	private void createObjects() {	
		
		planeRedisEntity = new PlaneRedisEntity();
		planeRedisEntity.setPlaneId(planeId);
		
	}
		
	@Test
	void PlaneRedisProjectorAdapter_existById_ReturnBooleTrue() {

		Mockito.when(planeRedisRepository.existsById(Mockito.any(UUID.class))).thenReturn(true);
		
		boolean resultValue = planeRedisProjectorAdapter.existById(UUID.randomUUID());
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isTrue()
				);
	}

	@Test
	void PlaneRedisProjectorAdapter_existById_ReturnBooleFalse() {

		Mockito.when(planeRedisRepository.existsById(Mockito.any(UUID.class))).thenReturn(false);
		
		boolean resultValue = planeRedisProjectorAdapter.existById(UUID.randomUUID());
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isFalse()
				);
	}

	@Test
	void PlaneRedisProjectorAdapter_save_ReturnVoid() {
		
		PlaneCreatedEvent event = new PlaneCreatedEvent(
				databaseSequence,
				planeId,
				name,
				capacity
				);
		Mockito.when(planeRedisRepository.save(Mockito.any(PlaneRedisEntity.class))).thenReturn(planeRedisEntity);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		planeRedisProjectorAdapter.save(event);
		
		Mockito.verify(planeRedisRepository, times(1)).save(Mockito.any(PlaneRedisEntity.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	
	}

	@Test
	void PlaneRedisProjectorAdapter_deleteById_ReturnVoid() {
		
		PlaneDeletedEvent event = new PlaneDeletedEvent(
				databaseSequence,
				planeId
				);
		Mockito.doNothing().when(planeRedisRepository).delete(Mockito.any(PlaneRedisEntity.class));
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		
		planeRedisProjectorAdapter.deleteById(event);
		
		Mockito.verify(planeRedisRepository, times(1)).delete(Mockito.any(PlaneRedisEntity.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());	
	}	
	
}
