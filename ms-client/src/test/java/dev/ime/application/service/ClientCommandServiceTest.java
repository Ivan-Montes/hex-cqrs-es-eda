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

import dev.ime.application.dispatch.ClientCommandDispatcher;
import dev.ime.application.dto.ClientDto;
import dev.ime.application.event.ClientCreatedEvent;
import dev.ime.application.event.ClientDeletedEvent;
import dev.ime.application.event.ClientUpdatedEvent;
import dev.ime.application.exception.EventUnexpectedException;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.PublisherPort;

@ExtendWith(MockitoExtension.class)
class ClientCommandServiceTest {

	@Mock
	private ClientCommandDispatcher clientCommandDispatcher;

	@Mock
	private PublisherPort publisherPort;
	
	@InjectMocks
	private ClientCommandService clientCommandService;

	@Mock
	private CommandHandler<Optional<Event>> commandHandler;
	
	private final UUID id = UUID.randomUUID();
	private final String name = "Triss";
	private final String lastname = "Merigold";
	private final Long databaseSequence = 9L;
	private ClientDto clientDtoTest;
	
	@BeforeEach
	private void createObjects() {
		
		clientDtoTest = new ClientDto(id, name, lastname);

	}
	
	
	@Test
	void ClientCommandService_create_ReturnOptionalEvent() {
		
		ClientCreatedEvent event = new ClientCreatedEvent(databaseSequence, id, name, lastname);
		doReturn(commandHandler).when(clientCommandDispatcher).getCommandHandler(Mockito.any(Command.class));
		Mockito.when(commandHandler.handle(Mockito.any(Command.class))).thenReturn(Optional.ofNullable(event));
		
		Optional<Event> optEvent = clientCommandService.create(clientDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(ClientCreatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}

	@Test
	void ClientCommandService_create_ReturnEventUnexpectedException() {
		
		doReturn(commandHandler).when(clientCommandDispatcher).getCommandHandler(Mockito.any(Command.class));
		Mockito.when(commandHandler.handle(Mockito.any(Command.class))).thenReturn(Optional.ofNullable(null));
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(EventUnexpectedException.class, ()-> clientCommandService.create(clientDtoTest));

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(EventUnexpectedException.class)
				);		
	}
	
	@Test
	void ClientCommandService_update_ReturnOptionalEvent() {

		ClientUpdatedEvent event = new ClientUpdatedEvent(databaseSequence, id, name, lastname);
		doReturn(commandHandler).when(clientCommandDispatcher).getCommandHandler(Mockito.any(Command.class));
		Mockito.when(commandHandler.handle(Mockito.any(Command.class))).thenReturn(Optional.ofNullable(event));
		
		Optional<Event> optEvent = clientCommandService.update(id, clientDtoTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(ClientUpdatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}
	

	@Test
	void ClientCommandService_deleteById_ReturnOptionalEvent() {
		
		ClientDeletedEvent event = new ClientDeletedEvent(databaseSequence, id);
		doReturn(commandHandler).when(clientCommandDispatcher).getCommandHandler(Mockito.any(Command.class));
		Mockito.when(commandHandler.handle(Mockito.any(Command.class))).thenReturn(Optional.ofNullable(event));
		
		Optional<Event> optEvent = clientCommandService.deleteById(id);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(ClientDeletedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}

}
