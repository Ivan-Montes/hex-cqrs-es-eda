package dev.ime.application.service;

import static org.mockito.Mockito.doReturn;

import java.time.LocalDate;
import java.time.LocalTime;
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

import dev.ime.application.dispatch.FlightCommandDispatcher;
import dev.ime.application.dto.FlightDto;
import dev.ime.application.event.FlightCreatedEvent;
import dev.ime.application.event.FlightDeletedEvent;
import dev.ime.application.event.FlightUpdatedEvent;
import dev.ime.application.exception.DateTimeBasicException;
import dev.ime.application.exception.EventUnexpectedException;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.PublisherPort;

@ExtendWith(MockitoExtension.class)
class FlightCommandServiceTest {

	@Mock
	private FlightCommandDispatcher flightCommandDispatcher;

	@Mock
	private PublisherPort publisherPort;

	@InjectMocks
	private FlightCommandService flightCommandService;

	@Mock
	private CommandHandler<Optional<Event>> commandHandler;
	
	private FlightDto flightDtoTest;
	private final Long databaseSequence = 11L;
	private final UUID flightId = UUID.randomUUID();
	private final String origin = "Madrid";
	private final String destiny = "Tokyo";
	private final LocalDate departureDate = LocalDate.parse("2024-04-01");
	private final LocalTime departureTime = LocalTime.parse("15:00");
	private final UUID planeId = UUID.randomUUID();	

	@BeforeEach
	private void createObjects() {
		
		flightDtoTest = new FlightDto(
				flightId,
				origin,
				destiny,
				departureDate.toString(),
				departureTime.toString(),
				planeId
				);
	}
	
	@Test
	void FlightCommandService_create_ReturnOptionalEvent() {
		
		FlightCreatedEvent event = new FlightCreatedEvent(
				databaseSequence,
				flightId,
				origin,
				destiny,
				departureDate,
				departureTime,
				planeId
				);
		doReturn(commandHandler).when(flightCommandDispatcher).getCommandHandler(Mockito.any(Command.class));
		Mockito.when(commandHandler.handle(Mockito.any(Command.class))).thenReturn(Optional.ofNullable(event));
		
		Optional<Event> optEvent = flightCommandService.create(flightDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(FlightCreatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}

	@Test
	void FlightCommandService_create_ReturnEventUnexpectedException() {
		
		doReturn(commandHandler).when(flightCommandDispatcher).getCommandHandler(Mockito.any(Command.class));
		Mockito.when(commandHandler.handle(Mockito.any(Command.class))).thenReturn(Optional.ofNullable(null));
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(EventUnexpectedException.class, ()-> flightCommandService.create(flightDtoTest));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(EventUnexpectedException.class)
				);
	}

	@Test
	void FlightCommandService_create_ReturnDateTimeBasicException() {
		
		FlightDto badFlight = new FlightDto();
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(DateTimeBasicException.class, ()-> flightCommandService.create(badFlight));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(DateTimeBasicException.class)
				);
	}

	@Test
	void FlightCommandService_update_ReturnOptionalEvent() {
		
		FlightUpdatedEvent event = new FlightUpdatedEvent(
				databaseSequence,
				flightId,
				origin,
				destiny,
				departureDate,
				departureTime,
				planeId
				);
		doReturn(commandHandler).when(flightCommandDispatcher).getCommandHandler(Mockito.any(Command.class));
		Mockito.when(commandHandler.handle(Mockito.any(Command.class))).thenReturn(Optional.ofNullable(event));
		
		Optional<Event> optEvent = flightCommandService.update(flightId, flightDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(FlightUpdatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}
	
	@Test
	void FlightCommandService_deleteById_ReturnOptionalEvent() {
		
		FlightDeletedEvent event = new FlightDeletedEvent(
				databaseSequence,
				flightId
				);
		doReturn(commandHandler).when(flightCommandDispatcher).getCommandHandler(Mockito.any(Command.class));
		Mockito.when(commandHandler.handle(Mockito.any(Command.class))).thenReturn(Optional.ofNullable(event));
		
		Optional<Event> optEvent = flightCommandService.deleteById(flightId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(FlightDeletedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}		

}
