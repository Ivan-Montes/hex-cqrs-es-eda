package dev.ime.infrastructure.adapter;


import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.event.PlaneCreatedEvent;
import dev.ime.application.event.PlaneDeletedEvent;
import dev.ime.application.event.PlaneUpdatedEvent;
import dev.ime.application.event.SeatCreatedEvent;
import dev.ime.application.event.SeatDeletedEvent;
import dev.ime.application.event.SeatUpdatedEvent;
import dev.ime.domain.event.Event;
import dev.ime.infrastructure.repository.write.EventNoSqlWriteRepository;

@ExtendWith(MockitoExtension.class)
class EventNoSqlWriteRepositoryAdapterTest {

	@Mock
	private EventNoSqlWriteRepository eventNoSqlWriteRepository;

	@InjectMocks
	private EventNoSqlWriteRepositoryAdapter eventNoSqlWriteRepositoryAdapter;

	private final Long databaseSequence = 27L;
	private final UUID planeId = UUID.randomUUID();
	private final String name = "Pursuit Special";
	private final Integer capacity = 2;
	private final UUID seatId = UUID.randomUUID();
	private final String seatNumber = "PS1973";

	@Test
	void EventNoSqlWriteRepositoryAdapter_save_ReturnOptionalPlaneCreatedEvent() {
		
		PlaneCreatedEvent event = new PlaneCreatedEvent(
				databaseSequence,
				planeId,
				name,
				capacity
				);
		Mockito.when(eventNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);

		Optional<Event> optEvent = eventNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(PlaneCreatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}
	
	@Test
	void EventNoSqlWriteRepositoryAdapter_save_ReturnOptionalPlaneUpdatedEvent() {
		
		PlaneUpdatedEvent event = new PlaneUpdatedEvent(
				databaseSequence,
				planeId,
				name,
				capacity
				);
		Mockito.when(eventNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);

		Optional<Event> optEvent = eventNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(PlaneUpdatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}

	@Test
	void EventNoSqlWriteRepositoryAdapter_save_ReturnOptionalPlaneDeletedEvent() {
		
		PlaneDeletedEvent event = new PlaneDeletedEvent(
				databaseSequence,
				planeId
				);
		Mockito.when(eventNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);

		Optional<Event> optEvent = eventNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(PlaneDeletedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}

	@Test
	void EventNoSqlWriteRepositoryAdapter_save_ReturnOptionalSeatCreatedEvent() {
		
		SeatCreatedEvent event = new SeatCreatedEvent(
				databaseSequence,
				seatId,
				seatNumber,
				planeId
				);
		Mockito.when(eventNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);

		Optional<Event> optEvent = eventNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(SeatCreatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}

	@Test
	void EventNoSqlWriteRepositoryAdapter_save_ReturnOptionalSeatUpdatedEvent() {
		
		SeatUpdatedEvent event = new SeatUpdatedEvent(
				databaseSequence,
				seatId,
				seatNumber,
				planeId
				);
		Mockito.when(eventNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);

		Optional<Event> optEvent = eventNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(SeatUpdatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}

	@Test
	void EventNoSqlWriteRepositoryAdapter_save_ReturnOptionalSeatDeletedEvent() {
		
		SeatDeletedEvent event = new SeatDeletedEvent(
				databaseSequence,
				seatId
				);
		Mockito.when(eventNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);

		Optional<Event> optEvent = eventNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(SeatDeletedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}
	
}
