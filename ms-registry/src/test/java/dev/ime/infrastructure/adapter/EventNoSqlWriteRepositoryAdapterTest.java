package dev.ime.infrastructure.adapter;


import java.time.LocalDate;
import java.time.LocalTime;
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

import dev.ime.application.event.*;
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
	private final UUID paymentId = UUID.randomUUID();
	private final UUID notificationId = UUID.randomUUID();
	private final UUID planeId = UUID.randomUUID();
	private final String name = "Pursuit Special";
	private final Integer capacity = 2;
	private final UUID seatId = UUID.randomUUID();
	private final String seatNumber = "PS1973";
	private final String origin = "Madrid";
	private final String destiny = "Tokyo";
	private final LocalDate departureDate = LocalDate.parse("2024-04-01");
	private final LocalTime departureTime = LocalTime.parse("15:00");
	private final String clientName = "Triss";
	private final String lastname = "Merigold";

	@Test
	void EventNoSqlWriteRepositoryAdapter_save_ClientCreatedEvent_ReturnOptionalEvent() {

		ClientCreatedEvent event = new ClientCreatedEvent(databaseSequence, clientId, clientName, lastname);
		Mockito.when(eventNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);
		
		Optional<Event> optEvent = eventNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(ClientCreatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}

	@Test
	void EventNoSqlWriteRepositoryAdapter_save_ClientUpdatedEvent_ReturnOptionalEvent() {

		ClientUpdatedEvent event = new ClientUpdatedEvent(databaseSequence, clientId, clientName, lastname);
		Mockito.when(eventNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);
		
		Optional<Event> optEvent = eventNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(ClientUpdatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}

	@Test
	void EventNoSqlWriteRepositoryAdapter_save_FClientDeletedEvent_ReturnOptionalEvent() {

		ClientDeletedEvent event = new ClientDeletedEvent(databaseSequence, clientId);

		Mockito.when(eventNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);
		
		Optional<Event> optEvent = eventNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(ClientDeletedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}

	@Test
	void EventNoSqlWriteRepositoryAdapter_save_FlightCreatedEvent_ReturnOptionalEvent() {

		FlightCreatedEvent event = new FlightCreatedEvent(
				databaseSequence,
				flightId,
				origin,
				destiny,
				departureDate,
				departureTime,
				planeId
				);
		Mockito.when(eventNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);
		
		Optional<Event> optEvent = eventNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(FlightCreatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}

	@Test
	void EventNoSqlWriteRepositoryAdapter_save_FlightUpdatedEvent_ReturnOptionalEvent() {

		FlightUpdatedEvent event = new FlightUpdatedEvent(
				databaseSequence,
				flightId,
				origin,
				destiny,
				departureDate,
				departureTime,
				planeId
				);		Mockito.when(eventNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);
		
		Optional<Event> optEvent = eventNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(FlightUpdatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}

	@Test
	void EventNoSqlWriteRepositoryAdapter_save_FlightDeletedEvent_ReturnOptionalEvent() {

		FlightDeletedEvent event = new FlightDeletedEvent(databaseSequence, flightId);

		Mockito.when(eventNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);
		
		Optional<Event> optEvent = eventNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(FlightDeletedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}

	@Test
	void EventNoSqlWriteRepositoryAdapter_save_PlaneCreatedEvent_ReturnOptionalEvent() {

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
	void EventNoSqlWriteRepositoryAdapter_save_PlaneUpdatedEvent_ReturnOptionalEvent() {

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
	void EventNoSqlWriteRepositoryAdapter_save_PlaneDeletedEvent_ReturnOptionalEvent() {

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
	void EventNoSqlWriteRepositoryAdapter_save_SeatCreatedEvent_ReturnOptionalEvent() {

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
	void EventNoSqlWriteRepositoryAdapter_save_SeatUpdatedEvent_ReturnOptionalEvent() {

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
	void EventNoSqlWriteRepositoryAdapter_save_SeatDeletedEvent_ReturnOptionalEvent() {

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

	@Test
	void EventNoSqlWriteRepositoryAdapter_save_ReservationCreatedEvent_ReturnOptionalEvent() {

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
	void EventNoSqlWriteRepositoryAdapter_save_ReservationUpdatedEvent_ReturnOptionalEvent() {

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
	void EventNoSqlWriteRepositoryAdapter_save_ReservationDeletedEvent_ReturnOptionalEvent() {

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

	@Test
	void EventNoSqlWriteRepositoryAdapter_save_PaymentCreatedEvent_ReturnOptionalEvent() {

		PaymentCreatedEvent event = new PaymentCreatedEvent(
				databaseSequence,
				paymentId,
				reservationId,
				clientId,
				flightId				
				);
		Mockito.when(eventNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);
		
		Optional<Event> optEvent = eventNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(PaymentCreatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}

	@Test
	void EventNoSqlWriteRepositoryAdapter_save_PaymentUpdatedEvent_ReturnOptionalEvent() {

		PaymentUpdatedEvent event = new PaymentUpdatedEvent(
				databaseSequence,
				paymentId,
				reservationId,
				clientId,
				flightId				
				);
		Mockito.when(eventNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);
		
		Optional<Event> optEvent = eventNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(PaymentUpdatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}

	@Test
	void EventNoSqlWriteRepositoryAdapter_save_PaymentDeletedEvent_ReturnOptionalEvent() {

		PaymentDeletedEvent event = new PaymentDeletedEvent(
				databaseSequence,
				paymentId			
				);
		Mockito.when(eventNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);
		
		Optional<Event> optEvent = eventNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(PaymentDeletedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}

	@Test
	void EventNoSqlWriteRepositoryAdapter_save_NotificationCreatedEvent_ReturnOptionalEvent() {

		NotificationCreatedEvent event = new NotificationCreatedEvent(
				databaseSequence,
				notificationId,
				paymentId			
				);
		Mockito.when(eventNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);
		
		Optional<Event> optEvent = eventNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(NotificationCreatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}

	@Test
	void EventNoSqlWriteRepositoryAdapter_save_NotificationUpdatedEvent_ReturnOptionalEvent() {

		NotificationUpdatedEvent event = new NotificationUpdatedEvent(
				databaseSequence,
				notificationId,
				paymentId			
				);
		Mockito.when(eventNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);
		
		Optional<Event> optEvent = eventNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(NotificationUpdatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}

	@Test
	void EventNoSqlWriteRepositoryAdapter_save_NotificationDeletedEvent_ReturnOptionalEvent() {

		NotificationDeletedEvent event = new NotificationDeletedEvent(
				databaseSequence,
				notificationId	
				);
		Mockito.when(eventNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);
		
		Optional<Event> optEvent = eventNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(NotificationDeletedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}

}
