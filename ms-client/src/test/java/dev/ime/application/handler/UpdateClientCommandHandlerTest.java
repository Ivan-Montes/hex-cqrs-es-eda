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

import dev.ime.application.event.ClientUpdatedEvent;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.DeleteClientCommand;
import dev.ime.application.usecase.UpdateClientCommand;
import dev.ime.domain.event.Event;
import dev.ime.domain.model.Client;
import dev.ime.domain.port.outbound.ClientNoSqlReadRepositoryPort;
import dev.ime.domain.port.outbound.ClientNoSqlWriteRepositoryPort;
import dev.ime.domain.port.outbound.DatabaseSequencePort;

@ExtendWith(MockitoExtension.class)
class UpdateClientCommandHandlerTest {

	@Mock
	private ClientNoSqlWriteRepositoryPort clientNoSqlWriteRepositoryPort;

	@Mock
	private DatabaseSequencePort databaseSequencePort;

	@Mock
	private ClientNoSqlReadRepositoryPort clientNoSqlReadRepositoryPort;
	
	@InjectMocks
	private UpdateClientCommandHandler updateClientCommandHandler;
	
	private UpdateClientCommand updateClientCommand;
	private DeleteClientCommand deleteClientCommand;
	private ClientUpdatedEvent event;
	private final UUID id = UUID.randomUUID();
	private final String name = "Triss";
	private final String lastname = "Merigold";
	private final Long databaseSequence = 9L;
	
	@BeforeEach
	private void createObjects() {

		updateClientCommand = new UpdateClientCommand(id, name, lastname);
		event = new ClientUpdatedEvent(databaseSequence, id, name, lastname);
		deleteClientCommand = new DeleteClientCommand(id);
		
	}
	
	@Test
	void UpdateClientCommandHandler_handle_ReturnOptionalEvent() {
		
		Mockito.when(clientNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(new Client()));
		Mockito.when(databaseSequencePort.generateSequence(Mockito.anyString())).thenReturn(databaseSequence);
		Mockito.when(clientNoSqlWriteRepositoryPort.save(Mockito.any(Event.class))).thenReturn(Optional.ofNullable(event));
		
		Optional<Event> optEvent = updateClientCommandHandler.handle(updateClientCommand);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(ClientUpdatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}	

	@Test
	void UpdateClientCommandHandler_handle_ReturnIllegalArgumentException() {
	
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> updateClientCommandHandler.handle(deleteClientCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}

	@Test
	void UpdateClientCommandHandler_handle_ReturnResourceNotFoundException() {
		
		Mockito.when(clientNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> updateClientCommandHandler.handle(updateClientCommand));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}

}
