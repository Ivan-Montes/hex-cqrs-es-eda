package dev.ime.application.handler;


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

import dev.ime.application.event.ClientCreatedEvent;
import dev.ime.application.usecase.CreateClientCommand;
import dev.ime.application.usecase.DeleteClientCommand;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.ClientNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.DatabaseSequencePort;

@ExtendWith(MockitoExtension.class)
class CreateClientCommandHandlerTest {

	@Mock
	private ClientNoSqlWriteRepositoryPort clientNoSqlWriteRepositoryPort;

	@Mock
	private DatabaseSequencePort databaseSequencePort;
	
	@InjectMocks
	private CreateClientCommandHandler createClientCommandHandler;

	private CreateClientCommand createClientCommand;
	private DeleteClientCommand deleteClientCommand;
	private ClientCreatedEvent event;
	private final UUID clientId = UUID.randomUUID();
	private final String name = "Triss";
	private final String lastname = "Merigold";
	private final Long databaseSequence = 9L;
	
	@BeforeEach
	private void createObjects() {

		createClientCommand = new CreateClientCommand(clientId, name, lastname);
		event = new ClientCreatedEvent(databaseSequence, clientId, name, lastname);
		deleteClientCommand = new DeleteClientCommand(clientId);
		
	}
	
	@Test
	void CreateClientCommandHandler_handle_ReturnOptionalEvent() {
		
		Mockito.when(databaseSequencePort.generateSequence(Mockito.anyString())).thenReturn(databaseSequence);
		Mockito.when(clientNoSqlWriteRepositoryPort.save(Mockito.any(Event.class))).thenReturn(Optional.ofNullable(event));
		
		Optional<Event> optEvent = createClientCommandHandler.handle(createClientCommand);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(ClientCreatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}
	
	
	@Test
	void CreateClientCommandHandler_handle_ReturnIllegalArgumentException() {
	
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> createClientCommandHandler.handle(deleteClientCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}
	
}
