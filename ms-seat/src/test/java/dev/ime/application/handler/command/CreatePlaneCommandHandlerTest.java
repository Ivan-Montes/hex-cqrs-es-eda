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

import dev.ime.application.event.PlaneCreatedEvent;
import dev.ime.application.usecase.command.CreatePlaneCommand;
import dev.ime.application.usecase.command.DeletePlaneCommand;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.domain.port.outbound.EventNoSqlWriteRepositoryPort;

@ExtendWith(MockitoExtension.class)
class CreatePlaneCommandHandlerTest {

	@Mock
	private EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort;
	@Mock
	private DatabaseSequencePort databaseSequencePort;

	@InjectMocks
	private CreatePlaneCommandHandler createPlaneCommandHandler;
	
	private CreatePlaneCommand createPlaneCommand;
	private DeletePlaneCommand deletePlaneCommand;
	private PlaneCreatedEvent event;
	private final Long databaseSequence = 27L;
	private final UUID planeId = UUID.randomUUID();
	private final String name = "Pursuit Special";
	private final Integer capacity = 2;
	
	@BeforeEach
	private void createObjects() {	
		
		createPlaneCommand = new CreatePlaneCommand(
				planeId,
				name,
				capacity
				);
		
		event = new PlaneCreatedEvent(
				databaseSequence,
				planeId,
				name,
				capacity
				);
		
		deletePlaneCommand = new DeletePlaneCommand(planeId);
	}		

	@Test
	void CreatePlaneCommandHandler_handle_ReturnOptionalEvent() {
		
		Mockito.when(databaseSequencePort.generateSequence(Mockito.anyString())).thenReturn(databaseSequence);
		Mockito.when(eventNoSqlWriteRepositoryPort.save(Mockito.any(Event.class))).thenReturn(Optional.ofNullable(event));
		
		Optional<Event> optEvent = createPlaneCommandHandler.handle(createPlaneCommand);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(PlaneCreatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}
	
	@Test
	void CreatePlaneCommandHandler_handle_ReturnIllegalArgumentException() {
	
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> createPlaneCommandHandler.handle(deletePlaneCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}
	

}
