package dev.ime.application.service;

import static org.mockito.Mockito.doReturn;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.dispatch.ReservationCommandDispatcher;
import dev.ime.application.dto.ReservationDto;
import dev.ime.application.event.ReservationCreatedEvent;
import dev.ime.application.event.ReservationDeletedEvent;
import dev.ime.application.event.ReservationUpdatedEvent;
import dev.ime.application.exception.EventUnexpectedException;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.PublisherPort;

@ExtendWith(MockitoExtension.class)
class ReservationCommandServiceTest {

	@Mock
	private ReservationCommandDispatcher reservationCommandDispatcher;
	
	@Mock
	private PublisherPort publisherPort;
	
	@InjectMocks
	private ReservationCommandService reservationCommandService;

	@Mock
	private CommandHandler<Optional<Event>> commandHandler;

	private final UUID reservationId = UUID.randomUUID();
	private final UUID clientId = UUID.randomUUID();
	private final UUID flightId = UUID.randomUUID();
	private Set<UUID> seatIdsSet;
	private final Long databaseSequence = 37L;
	private ReservationDto reservationDtoTest;
	
	@BeforeEach
	private void createObjects() {
		
		reservationDtoTest = new ReservationDto(
				reservationId,
				clientId,
				flightId,
				seatIdsSet
				);
		
	}
		
	@Test
	void ReservationCommandService_create_ReturnOptionalEvent() {
		
		ReservationCreatedEvent event = new ReservationCreatedEvent(
				databaseSequence,
				reservationId,
				clientId,
				flightId,
				seatIdsSet);
		doReturn(commandHandler).when(reservationCommandDispatcher).getCommandHandler(Mockito.any(Command.class));
		Mockito.when(commandHandler.handle(Mockito.any(Command.class))).thenReturn(Optional.ofNullable(event));
		
		Optional<Event> optEvent = reservationCommandService.create(reservationDtoTest);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(ReservationCreatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}
	
	@Test
	void ReservationCommandService_create_ReturnEventUnexpectedException() {

		doReturn(commandHandler).when(reservationCommandDispatcher).getCommandHandler(Mockito.any(Command.class));
		Mockito.when(commandHandler.handle(Mockito.any(Command.class))).thenReturn(Optional.ofNullable(null));
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(EventUnexpectedException.class, ()-> reservationCommandService.create(reservationDtoTest));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(EventUnexpectedException.class)
				);	
	}

	@Test
	void ReservationCommandService_update_ReturnOptionalEvent() {
		
		ReservationUpdatedEvent event = new ReservationUpdatedEvent(
				databaseSequence,
				reservationId,
				clientId,
				flightId,
				seatIdsSet);
		doReturn(commandHandler).when(reservationCommandDispatcher).getCommandHandler(Mockito.any(Command.class));
		Mockito.when(commandHandler.handle(Mockito.any(Command.class))).thenReturn(Optional.ofNullable(event));
		
		Optional<Event> optEvent = reservationCommandService.update(reservationId, reservationDtoTest);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(ReservationUpdatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}

	@Test
	void ReservationCommandService_delete_ReturnOptionalEvent() {
		
		ReservationDeletedEvent event = new ReservationDeletedEvent(databaseSequence, reservationId);
		doReturn(commandHandler).when(reservationCommandDispatcher).getCommandHandler(Mockito.any(Command.class));
		Mockito.when(commandHandler.handle(Mockito.any(Command.class))).thenReturn(Optional.ofNullable(event));
		
		Optional<Event> optEvent = reservationCommandService.deleteById(reservationId);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(ReservationDeletedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
		
	}
	
}
