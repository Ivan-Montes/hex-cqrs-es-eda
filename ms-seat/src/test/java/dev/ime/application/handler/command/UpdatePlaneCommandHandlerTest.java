package dev.ime.application.handler.command;


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

import dev.ime.application.event.PlaneUpdatedEvent;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.command.DeletePlaneCommand;
import dev.ime.application.usecase.command.UpdatePlaneCommand;
import dev.ime.domain.event.Event;
import dev.ime.domain.model.Plane;
import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.domain.port.outbound.EventNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.GenericNoSqlReadRepositoryPort;

@ExtendWith(MockitoExtension.class)
class UpdatePlaneCommandHandlerTest {

	@Mock
	private EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort;
	
	@Mock
	private DatabaseSequencePort databaseSequencePort;

	@Mock
	private GenericNoSqlReadRepositoryPort<Plane> genericNoSqlReadRepositoryPort;
	
	@InjectMocks
	private UpdatePlaneCommandHandler updatePlaneCommandHandler;

	private UpdatePlaneCommand updatePlaneCommand;
	private DeletePlaneCommand deletePlaneCommand;
	private PlaneUpdatedEvent event;
	private final Long databaseSequence = 27L;
	private final UUID planeId = UUID.randomUUID();
	private final String name = "Pursuit Special";
	private final Integer capacity = 2;
	
	@BeforeEach
	private void createObjects() {	
		
		updatePlaneCommand = new UpdatePlaneCommand(
				planeId,
				name,
				capacity
				);
		
		event = new PlaneUpdatedEvent(
				databaseSequence,
				planeId,
				name,
				capacity
				);
		
		deletePlaneCommand = new DeletePlaneCommand(planeId);
	}

	@Test
	void UpdatePlaneCommandHandler_handle_ReturnOptionalEvent() {
		
		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(new Plane()));
		Mockito.when(databaseSequencePort.generateSequence(Mockito.anyString())).thenReturn(databaseSequence);
		Mockito.when(eventNoSqlWriteRepositoryPort.save(Mockito.any(Event.class))).thenReturn(Optional.ofNullable(event));
		
		Optional<Event> optEvent = updatePlaneCommandHandler.handle(updatePlaneCommand);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(PlaneUpdatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}
	
	@Test
	void UpdatePlaneCommandHandler_handle_ReturnIllegalArgumentException() {
	
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> updatePlaneCommandHandler.handle(deletePlaneCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}

	@Test
	void UpdatePlaneCommandHandler_handle_ReturnResourceNotFoundException() {
		
		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));

		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> updatePlaneCommandHandler.handle(updatePlaneCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}

}
