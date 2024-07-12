package dev.ime.infrastructure.adapter;


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
import dev.ime.application.event.ReservationUpdatedEvent;
import dev.ime.application.event.ReservationDeletedEvent;
import dev.ime.domain.event.Event;
import dev.ime.infrastructure.repository.EventNoSqlWriteRepository;

@ExtendWith(MockitoExtension.class)
class EventNoSqlWriteRepositoryAdapterTest {

	@Mock
	private EventNoSqlWriteRepository eventNoSqlWriteRepository;

	@InjectMocks
	private EventNoSqlWriteRepositoryAdapter eventNoSqlWriteRepositoryAdapter;

	private final UUID reservationId = UUID.randomUUID();
	private final UUID clientId = UUID.randomUUID();
	private final UUID flightId = UUID.randomUUID();
	private Set<UUID> seatIdsSet = new HashSet<>();
	private final Long databaseSequence = 37L;
	
	@Test
	void EventNoSqlWriteRepositoryAdapter_save_ReturnOptionalEvent() {

		ReservationCreatedEvent event = new ReservationCreatedEvent(
				databaseSequence,
				reservationId,
				clientId,
				flightId,
				seatIdsSet
				);
		Mockito.when(eventNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);
		
		Optional<Event> optEvent = eventNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(ReservationCreatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}

	@Test
	void EventNoSqlWriteRepositoryAdapter_update_ReturnOptionalEvent() {

		ReservationUpdatedEvent event = new ReservationUpdatedEvent(
				databaseSequence,
				reservationId,
				clientId,
				flightId,
				seatIdsSet
				);
		Mockito.when(eventNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);
		
		Optional<Event> optEvent = eventNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(ReservationUpdatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}

	@Test
	void EventNoSqlWriteRepositoryAdapter_delete_ReturnOptionalEvent() {

		ReservationDeletedEvent event = new ReservationDeletedEvent(
				databaseSequence,
				reservationId
				);
		Mockito.when(eventNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);
		
		Optional<Event> optEvent = eventNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(ReservationDeletedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}

}
