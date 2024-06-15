package dev.ime.infrastructure.adapter;


import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.event.ClientCreatedEvent;
import dev.ime.application.event.ClientDeletedEvent;
import dev.ime.application.event.ClientUpdatedEvent;
import dev.ime.domain.event.Event;
import dev.ime.infrastructure.repository.write.ClientNoSqlWriteRepository;

@ExtendWith(MockitoExtension.class)
class ClientNoSqlWriteRepositoryAdapterTest {

	@Mock
	private ClientNoSqlWriteRepository clientNoSqlWriteRepository;

	@InjectMocks
	private ClientNoSqlWriteRepositoryAdapter clientNoSqlWriteRepositoryAdapter;	

	private final UUID id = UUID.randomUUID();
	private final String name = "Triss";
	private final String lastname = "Merigold";
	private final Long databaseSequence = 9L;
	
	@Test
	void ClientNoSqlWriteRepositoryAdapter_save_ReturnOptionalEvent() {
		
		ClientCreatedEvent event = new ClientCreatedEvent(databaseSequence, id, name, lastname);
		Mockito.when(clientNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);
	
		Optional<Event> optEvent = clientNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(ClientCreatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence)
				);
	}
	
	@Test
	void ClientNoSqlWriteRepositoryAdapter_save_ReturnOptionalUpdateEvent() {
		
		ClientUpdatedEvent event = new ClientUpdatedEvent(databaseSequence, id, name, lastname);
		Mockito.when(clientNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);
	
		Optional<Event> optEvent = clientNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(ClientUpdatedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}
	
	@Test
	void ClientNoSqlWriteRepositoryAdapter_save_ReturnOptionalDeleteEvent() {
		
		ClientDeletedEvent event = new ClientDeletedEvent(databaseSequence, id);
		Mockito.when(clientNoSqlWriteRepository.save(Mockito.any(Event.class))).thenReturn(event);
	
		Optional<Event> optEvent = clientNoSqlWriteRepositoryAdapter.save(event);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optEvent).isNotEmpty(),
				()-> Assertions.assertThat(optEvent.get().getClass()).isEqualTo(ClientDeletedEvent.class),
				()-> Assertions.assertThat(optEvent.get().getSequence()).isEqualTo(databaseSequence),
				()-> Assertions.assertThat(optEvent.get().toString()).hasToString(event.toString())
				);
	}
}
