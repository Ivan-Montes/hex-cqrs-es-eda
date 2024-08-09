package dev.ime.application.handler;


import java.util.HashMap;
import java.util.Map;
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

import dev.ime.application.event.RegistryCreatedEvent;
import dev.ime.application.usecase.CreateRegistryCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.DatabaseSequencePort;
import dev.ime.domain.port.outbound.EventNoSqlWriteRepositoryPort;

@ExtendWith(MockitoExtension.class)
class CreateRegistryCommandHandlerTest {

	@Mock
	private EventNoSqlWriteRepositoryPort eventNoSqlWriteRepositoryPort;
	
	@Mock
	private DatabaseSequencePort databaseSequencePort;	
	
	@InjectMocks
	private CreateRegistryCommandHandler createRegistryCommandHandler;
	
	private CreateRegistryCommand createRegistryCommand;
	private RegistryCreatedEvent registryCreatedEvent;
	private class CommandTest implements Command{}	
	private final UUID registryId = UUID.randomUUID();
	private Map<String, Object> eventData;
	private final Long databaseSequence = 73L;
	
	@BeforeEach
	private void createObjects() {
		
		eventData = new HashMap<>();
		
		createRegistryCommand = new CreateRegistryCommand(
				registryId,
				eventData
				);
		registryCreatedEvent = new RegistryCreatedEvent(
				databaseSequence,
				eventData);
		
	}
		
	@Test
	void CreateRegistryCommandHandler_handle_ReturnOptionalEvent() {
		
		Mockito.when(databaseSequencePort.generateSequence(Mockito.anyString())).thenReturn(databaseSequence);		
		Mockito.when(eventNoSqlWriteRepositoryPort.save(Mockito.any(Event.class))).thenReturn(Optional.ofNullable(registryCreatedEvent));	
		
		Optional<Event> optEvent = createRegistryCommandHandler.handle(createRegistryCommand);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(RegistryCreatedEvent.class)
				);
	}

	@Test
	void CreateRegistryCommandHandler_handle_ReturnIllegalArgumentException() {
	
		CommandTest commandTest = new CommandTest();
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> createRegistryCommandHandler.handle(commandTest));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}

	

}
