package dev.ime.infrastructure.adapter;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.event.FlightCreatedEvent;
import dev.ime.application.event.FlightDeletedEvent;
import dev.ime.application.event.FlightUpdatedEvent;
import dev.ime.domain.event.Event;
import dev.ime.infrastructure.repository.write.FlightNoSqlWriteRepository;

@ExtendWith(MockitoExtension.class)
class FlightNoSqlWriteRepositoryAdapterTest {
	
	@Mock
	private FlightNoSqlWriteRepository flightNoSqlWriteRepository;

	@InjectMocks
	private FlightNoSqlWriteRepositoryAdapter flightNoSqlWriteRepositoryAdapter;	

	private final Long databaseSequence = 11L;
	private final UUID flightId = UUID.randomUUID();
	private final String origin = "Madrid";
	private final String destiny = "Tokyo";
	private final LocalDate departureDate = LocalDate.parse("2024-04-01");
	private final LocalTime departureTime = LocalTime.parse("15:00");
	private final UUID planeId = UUID.randomUUID();
	
	@Test
	void FlightNoSqlWriteRepositoryAdapter_save_ReturnOptionalEventDelete() {
		
		FlightDeletedEvent event = new FlightDeletedEvent(databaseSequence, flightId);
		Mockito.when(flightNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);

		Optional<Event> optEvent = flightNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(FlightDeletedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}

	@Test
	void FlightNoSqlWriteRepositoryAdapter_save_ReturnOptionalUpdateEvent() {
		
		FlightUpdatedEvent event = new FlightUpdatedEvent(
				databaseSequence,
				flightId,
				origin,
				destiny,
				departureDate,
				departureTime,
				planeId
				);
		Mockito.when(flightNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);

		Optional<Event> optEvent = flightNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(FlightUpdatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}

	@Test
	void FlightNoSqlWriteRepositoryAdapter_save_ReturnOptionalCreateEvent() {
		
		FlightCreatedEvent event = new FlightCreatedEvent(
				databaseSequence,
				flightId,
				origin,
				destiny,
				departureDate,
				departureTime,
				planeId
				);
		Mockito.when(flightNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);

		Optional<Event> optEvent = flightNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(FlightCreatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}
}
