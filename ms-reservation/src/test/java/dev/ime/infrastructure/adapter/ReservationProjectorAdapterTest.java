package dev.ime.infrastructure.adapter;

import static org.mockito.Mockito.times;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.assertj.core.api.Assertions;
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
import dev.ime.infrastructure.entity.ReservationJpaEntity;
import dev.ime.infrastructure.repository.ReservationJpaRepository;

@ExtendWith(MockitoExtension.class)
class ReservationProjectorAdapterTest {

	@Mock
	private LoggerUtil loggerUtil;
	
	@Mock
	private ReservationJpaRepository reservationJpaRepository;
	
	@InjectMocks
	private ReservationProjectorAdapter reservationProjectorAdapter;

	private final UUID reservationId = UUID.randomUUID();
	private final UUID clientId = UUID.randomUUID();
	private final UUID flightId = UUID.randomUUID();
	private Set<UUID> seatIdsSet = new HashSet<>();
	private final Long databaseSequence = 37L;
	
	@Test
	void ReservationProjectorAdapter_create_ReturnVoid() {
		
		ReservationCreatedEvent reservationCreatedEvent = new ReservationCreatedEvent(
				databaseSequence,
				reservationId,
				clientId,
				flightId,
				seatIdsSet
				);
		Mockito.when(reservationJpaRepository.save(Mockito.any(ReservationJpaEntity.class))).thenReturn(new ReservationJpaEntity());
		
		reservationProjectorAdapter.create(reservationCreatedEvent);
		
		Mockito.verify(reservationJpaRepository, times(1)).save(Mockito.any(ReservationJpaEntity.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());		
	}

	@Test
	void ReservationProjectorAdapter_deleteById_ReturnVoid() {
		
		ReservationDeletedEvent reservationDeletedEvent = new ReservationDeletedEvent(databaseSequence, reservationId);
		Mockito.when(reservationJpaRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(new ReservationJpaEntity()));
		
		reservationProjectorAdapter.deleteById(reservationDeletedEvent);
		
		Mockito.verify(reservationJpaRepository, times(1)).delete(Mockito.any(ReservationJpaEntity.class));	
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());		
	}

	@Test
	void ReservationProjectorAdapter_deleteById_ReturnVoidByOptEmpty() {
		
		ReservationDeletedEvent reservationDeletedEvent = new ReservationDeletedEvent(databaseSequence, reservationId);
		Mockito.when(reservationJpaRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable( null ));
		
		reservationProjectorAdapter.deleteById(reservationDeletedEvent);
		
		Mockito.verify(reservationJpaRepository, times(1)).findById(Mockito.any(UUID.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());		
	}

	@Test
	void ReservationProjectorAdapter_update_ReturnVoid() {
		
		ReservationUpdatedEvent reservationUpdatedEvent = new ReservationUpdatedEvent(
				databaseSequence,
				reservationId,
				clientId,
				flightId,
				seatIdsSet
				);
		Mockito.when(reservationJpaRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(new ReservationJpaEntity()));
		Mockito.when(reservationJpaRepository.save(Mockito.any(ReservationJpaEntity.class))).thenReturn(new ReservationJpaEntity());

		reservationProjectorAdapter.update(reservationUpdatedEvent);

		Mockito.verify(reservationJpaRepository, times(1)).findById(Mockito.any(UUID.class));
		Mockito.verify(reservationJpaRepository, times(1)).save(Mockito.any(ReservationJpaEntity.class));
	}

	@Test
	void ReservationProjectorAdapter_update_ReturnVoidByOptEmpty() {
		
		ReservationUpdatedEvent reservationUpdatedEvent = new ReservationUpdatedEvent(
				databaseSequence,
				reservationId,
				clientId,
				flightId,
				seatIdsSet
				);
		Mockito.when(reservationJpaRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));

		reservationProjectorAdapter.update(reservationUpdatedEvent);

		Mockito.verify(reservationJpaRepository, times(1)).findById(Mockito.any(UUID.class));
	}
	
	@Test
	void ReservationProjectorAdapter_existById_ReturnBoolean() {
		
		Mockito.when(reservationJpaRepository.existsById(Mockito.any(UUID.class))).thenReturn( true );

		boolean resultValue = reservationProjectorAdapter.existsById(reservationId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isTrue()
				);	
		Mockito.verify(reservationJpaRepository, times(1)).existsById(Mockito.any(UUID.class));

	}
	
}
