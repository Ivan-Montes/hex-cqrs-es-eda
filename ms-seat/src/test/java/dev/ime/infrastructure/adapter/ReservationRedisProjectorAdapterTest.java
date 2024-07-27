package dev.ime.infrastructure.adapter;

import static org.mockito.Mockito.times;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.event.ReservationCreatedEvent;
import dev.ime.application.event.ReservationDeletedEvent;
import dev.ime.application.event.ReservationUpdatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.infrastructure.entity.ReservationRedisEntity;
import dev.ime.infrastructure.repository.ReservationRedisRepository;

@ExtendWith(MockitoExtension.class)
class ReservationRedisProjectorAdapterTest {

	@Mock
	private LoggerUtil loggerUtil;
	
	@Mock
	private ReservationRedisRepository reservationRedisRepository;

	@InjectMocks
	private ReservationRedisProjectorAdapter reservationRedisProjectorAdapter;

	private final UUID reservationId = UUID.randomUUID();
	private final UUID clientId = UUID.randomUUID();
	private final UUID flightId = UUID.randomUUID();
	private Set<UUID> seatIdsSet = new HashSet<>();
	private final Long databaseSequence = 37L;	
	
	@Test
	void ReservationRedisProjectorAdapter_create_ReturnVoid() {
		
		ReservationCreatedEvent reservationCreatedEvent = new ReservationCreatedEvent(
				databaseSequence,
				reservationId,
				clientId,
				flightId,
				seatIdsSet
				);
		Mockito.when(reservationRedisRepository.save(Mockito.any(ReservationRedisEntity.class))).thenReturn(new ReservationRedisEntity());
		
		reservationRedisProjectorAdapter.create(reservationCreatedEvent);
		
		Mockito.verify(reservationRedisRepository, times(1)).save(Mockito.any(ReservationRedisEntity.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());		
	}

	@Test
	void ReservationRedisProjectorAdapter_update_ReturnVoid() {
		
		ReservationUpdatedEvent reservationUpdatedEvent = new ReservationUpdatedEvent(
				databaseSequence,
				reservationId,
				clientId,
				flightId,
				seatIdsSet
				);
		Mockito.when(reservationRedisRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(new ReservationRedisEntity()));
		Mockito.when(reservationRedisRepository.save(Mockito.any(ReservationRedisEntity.class))).thenReturn(new ReservationRedisEntity());

		reservationRedisProjectorAdapter.update(reservationUpdatedEvent);

		Mockito.verify(reservationRedisRepository, times(1)).findById(Mockito.any(UUID.class));
		Mockito.verify(reservationRedisRepository, times(1)).save(Mockito.any(ReservationRedisEntity.class));
	}

	@Test
	void ReservationRedisProjectorAdapter_update_ReturnVoidByOptEmpty() {
		
		ReservationUpdatedEvent reservationUpdatedEvent = new ReservationUpdatedEvent(
				databaseSequence,
				reservationId,
				clientId,
				flightId,
				seatIdsSet
				);
		Mockito.when(reservationRedisRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));

		reservationRedisProjectorAdapter.update(reservationUpdatedEvent);

		Mockito.verify(reservationRedisRepository, times(1)).findById(Mockito.any(UUID.class));
	}

	@Test
	void ReservationRedisProjectorAdapter_deleteById_ReturnVoid() {
		
		ReservationDeletedEvent reservationDeletedEvent = new ReservationDeletedEvent(databaseSequence, reservationId);
		Mockito.when(reservationRedisRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(new ReservationRedisEntity()));
		
		reservationRedisProjectorAdapter.deleteById(reservationDeletedEvent);
		
		Mockito.verify(reservationRedisRepository, times(1)).delete(Mockito.any(ReservationRedisEntity.class));	
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());		
	}

	@Test
	void ReservationRedisProjectorAdapter_deleteById_ReturnVoidByOptEmpty() {
		
		ReservationDeletedEvent reservationDeletedEvent = new ReservationDeletedEvent(databaseSequence, reservationId);
		Mockito.when(reservationRedisRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable( null ));
		
		reservationRedisProjectorAdapter.deleteById(reservationDeletedEvent);
		
		Mockito.verify(reservationRedisRepository, times(1)).findById(Mockito.any(UUID.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());		
	}

}
