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

import dev.ime.application.dispatch.PlaneCommandDispatcher;
import dev.ime.application.dto.PlaneDto;
import dev.ime.application.event.PlaneCreatedEvent;
import dev.ime.application.event.PlaneDeletedEvent;
import dev.ime.application.event.PlaneUpdatedEvent;
import dev.ime.application.exception.EventUnexpectedException;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.PublisherPort;

@ExtendWith(MockitoExtension.class)
class PlaneCommandServiceTest {

	@Mock
	private PlaneCommandDispatcher planeCommandDispatcher;
	
	@Mock
	private PublisherPort publisherPort;
	
	@InjectMocks
	private PlaneCommandService planeCommandService;

	@Mock
	private CommandHandler<Optional<Event>> commandHandler;
	
	private PlaneDto planeDtoTest;
	private final Long databaseSequence = 27L;
	private final UUID planeId = UUID.randomUUID();
	private final String name = "Pursuit Special";
	private final Integer capacity = 2;

	@BeforeEach
	private void createObjects() {
		
		planeDtoTest = new PlaneDto(
				planeId,
				name,
				capacity
				);
	}
	
	@Test
	void PlaneCommandService_create_ReturnOptionalEvent() {
		
		PlaneCreatedEvent event = new PlaneCreatedEvent(
				databaseSequence,
				planeId,
				name,
				capacity);
		doReturn(commandHandler).when(planeCommandDispatcher).getCommandHandler(Mockito.any(Command.class));
		Mockito.when(commandHandler.handle(Mockito.any(Command.class))).thenReturn(Optional.ofNullable(event));

		Optional<Event> optEvent = planeCommandService.create(planeDtoTest);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(PlaneCreatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}

	@Test
	void PlaneCommandService_create_ReturnEventUnexpectedException() {
		
		doReturn(commandHandler).when(planeCommandDispatcher).getCommandHandler(Mockito.any(Command.class));
		Mockito.when(commandHandler.handle(Mockito.any(Command.class))).thenReturn(Optional.ofNullable(null));
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(EventUnexpectedException.class, ()-> planeCommandService.create(planeDtoTest));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(EventUnexpectedException.class)
				);
	}

	@Test
	void PlaneCommandService_update_ReturnOptionalEvent() {
		
		PlaneUpdatedEvent event = new PlaneUpdatedEvent(
				databaseSequence,
				planeId,
				name,
				capacity);
		doReturn(commandHandler).when(planeCommandDispatcher).getCommandHandler(Mockito.any(Command.class));
		Mockito.when(commandHandler.handle(Mockito.any(Command.class))).thenReturn(Optional.ofNullable(event));
		
		Optional<Event> optEvent = planeCommandService.update(planeId, planeDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(PlaneUpdatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}

	@Test
	void PlaneCommandService_deleteById_ReturnOptionalEvent() {
		
		PlaneDeletedEvent event = new PlaneDeletedEvent(
				databaseSequence,
				planeId);
		doReturn(commandHandler).when(planeCommandDispatcher).getCommandHandler(Mockito.any(Command.class));
		Mockito.when(commandHandler.handle(Mockito.any(Command.class))).thenReturn(Optional.ofNullable(event));
		
		Optional<Event> optEvent = planeCommandService.deleteById(planeId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(PlaneDeletedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}	
	
}
