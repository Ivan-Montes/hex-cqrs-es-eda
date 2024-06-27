package dev.ime.infrastructure.adapter;

import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
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
import dev.ime.infrastructure.entity.SeatRedisEntity;
import dev.ime.infrastructure.repository.SeatRedisRepository;

@ExtendWith(MockitoExtension.class)
class SeatRedisProjectorAdapterTest {

	@Mock
	private LoggerUtil loggerUtil;
	
	@Mock
	private SeatRedisRepository seatRedisRepository;

	@InjectMocks
	private SeatRedisProjectorAdapter seatRedisProjectorAdapter;

	private final Long databaseSequence = 27L;
	private final UUID seatId = UUID.randomUUID();
	private final String seatNumber = "PS1973";
	private final UUID planeId = UUID.randomUUID();
	private List<SeatRedisEntity> seatRedisEntityList = new ArrayList<>();
	
	@Test
	void SeatRedisProjectorAdapter_create_ReturnVoid() {
		
		SeatCreatedEvent event = new SeatCreatedEvent(
				databaseSequence,
				seatId,
				seatNumber,
				planeId
				);
		Mockito.when(seatRedisRepository.save(Mockito.any(SeatRedisEntity.class))).thenReturn(new SeatRedisEntity());
		
		seatRedisProjectorAdapter.create(event);
		
		Mockito.verify(seatRedisRepository, times(1)).save(Mockito.any(SeatRedisEntity.class));
	}

	@Test
	void SeatRedisProjectorAdapter_update_ReturnVoid() {
		
		SeatUpdatedEvent event = new SeatUpdatedEvent(
				databaseSequence,
				seatId,
				seatNumber,
				planeId
				);
		Mockito.when(seatRedisRepository.save(Mockito.any(SeatRedisEntity.class))).thenReturn(new SeatRedisEntity());
		
		seatRedisProjectorAdapter.update(event);
		
		Mockito.verify(seatRedisRepository, times(1)).save(Mockito.any(SeatRedisEntity.class));
	}

	@Test
	void SeatRedisProjectorAdapter_deleteById_ReturnVoid() {

		SeatDeletedEvent event = new SeatDeletedEvent(
				databaseSequence,
				seatId
				);
		Mockito.doNothing().when(seatRedisRepository).deleteById(Mockito.any(UUID.class));

		seatRedisProjectorAdapter.deleteById(event);
		
		Mockito.verify(seatRedisRepository, times(1)).deleteById(Mockito.any(UUID.class));
	}

	@Test
	void SeatRedisProjectorAdapter_existById_ReturnBooleanFalse() {
		
		Mockito.when(seatRedisRepository.existsById(Mockito.any(UUID.class))).thenReturn(false);
		
		boolean resultValue = seatRedisProjectorAdapter.existsById(UUID.randomUUID());
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isFalse()
				);	
	}

	@Test
	void SeatRedisProjectorAdapter_findSeatByPlaneId_ReturnListUUID() {
		
		seatRedisEntityList.add(new SeatRedisEntity(seatId, planeId));
		Mockito.when(seatRedisRepository.findByPlaneId(Mockito.any(UUID.class))).thenReturn(seatRedisEntityList);

		List<UUID> list =  seatRedisProjectorAdapter.findSeatByPlaneId(planeId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0)).isEqualTo(seatId)
				);
	}

}
