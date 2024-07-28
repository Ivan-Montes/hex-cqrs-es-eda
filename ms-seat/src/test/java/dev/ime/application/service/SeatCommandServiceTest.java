package dev.ime.application.service;

import static org.mockito.Mockito.doReturn;

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

import dev.ime.application.dispatch.SeatCommandDispatcher;
import dev.ime.application.dto.SeatDto;
import dev.ime.application.event.SeatCreatedEvent;
import dev.ime.application.event.SeatDeletedEvent;
import dev.ime.application.event.SeatUpdatedEvent;
import dev.ime.application.exception.EventUnexpectedException;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.PublisherPort;

@ExtendWith(MockitoExtension.class)
class SeatCommandServiceTest {

	@Mock
	private SeatCommandDispatcher seatCommandDispatcher;
	
	@Mock
	private PublisherPort publisherPort;

	@InjectMocks
	private SeatCommandService seatCommandService;

	@Mock
	private CommandHandler<Optional<Event>> commandHandler;
	
	private SeatDto seatDtoTest;
	private final Long databaseSequence = 27L;
	private final UUID seatId = UUID.randomUUID();
	private final String seatNumber = "PS1973";
	private final UUID planeId = UUID.randomUUID();

	@BeforeEach
	private void createObjects() {
		
		seatDtoTest = new SeatDto(
				seatId,
				seatNumber,
				planeId);
		
	}
	
	@Test
	void SeatCommandService_create_ReturnOptionalEvent() {
		
		SeatCreatedEvent event = new SeatCreatedEvent(
				databaseSequence,
				seatId,
				seatNumber,
				planeId
				);
		doReturn(commandHandler).when(seatCommandDispatcher).getCommandHandler(Mockito.any(Command.class));
		Mockito.when(commandHandler.handle(Mockito.any(Command.class))).thenReturn(Optional.ofNullable(event));
		
		Optional<Event> optEvent = seatCommandService.create(seatDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(SeatCreatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}

	@Test
	void SeatCommandService_create_ReturnEventUnexpectedException() {
		
		doReturn(commandHandler).when(seatCommandDispatcher).getCommandHandler(Mockito.any(Command.class));
		Mockito.when(commandHandler.handle(Mockito.any(Command.class))).thenReturn(Optional.ofNullable(null));
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(EventUnexpectedException.class, ()-> seatCommandService.create(seatDtoTest));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(EventUnexpectedException.class)
				);
	}

	@Test
	void SeatCommandService_update_ReturnOptionalEvent() {
	
		SeatUpdatedEvent event = new SeatUpdatedEvent(
				databaseSequence,
				seatId,
				seatNumber,
				planeId
				);

		doReturn(commandHandler).when(seatCommandDispatcher).getCommandHandler(Mockito.any(Command.class));
		Mockito.when(commandHandler.handle(Mockito.any(Command.class))).thenReturn(Optional.ofNullable(event));

		Optional<Event> optEvent = seatCommandService.update(seatId, seatDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(SeatUpdatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}
	
	@Test
	void SeatCommandService_deleteById_ReturnOptionalEvent() {
		
		SeatDeletedEvent event = new SeatDeletedEvent(
				databaseSequence,
				seatId
				);
		doReturn(commandHandler).when(seatCommandDispatcher).getCommandHandler(Mockito.any(Command.class));
		Mockito.when(commandHandler.handle(Mockito.any(Command.class))).thenReturn(Optional.ofNullable(event));
		
		Optional<Event> optEvent = seatCommandService.deleteById(seatId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(SeatDeletedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}
	
}
