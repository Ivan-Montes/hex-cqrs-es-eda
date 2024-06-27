package dev.ime.infrastructure.adapter;

import static org.mockito.Mockito.times;

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
import dev.ime.config.LoggerUtil;
import dev.ime.infrastructure.entity.ClientRedisEntity;
import dev.ime.infrastructure.repository.ClientRedisRepository;

@ExtendWith(MockitoExtension.class)
class ClientRedisProjectorAdapterTest {

	@Mock
	private LoggerUtil loggerUtil;
	@Mock
	private ClientRedisRepository clientRedisRepository;

	@InjectMocks
	private ClientRedisProjectorAdapter clientRedisProjectorAdapter;

	private final UUID clientId = UUID.randomUUID();
	private final String name = "Triss";
	private final String lastname = "Merigold";
	private final Long databaseSequence = 37L;
	
	@Test
	void ClientRedisProjectorAdapter_create_ReturnVoid() {
		
		ClientCreatedEvent event = new ClientCreatedEvent(databaseSequence, clientId, name, lastname);
		Mockito.when(clientRedisRepository.save(Mockito.any(ClientRedisEntity.class))).thenReturn(new ClientRedisEntity());
	
		clientRedisProjectorAdapter.create(event);
		
		Mockito.verify(clientRedisRepository, times(1)).save(Mockito.any(ClientRedisEntity.class));
	}

	@Test
	void ClientRedisProjectorAdapter_deleteById_ReturnVoid() {
		
		ClientDeletedEvent event = new ClientDeletedEvent(databaseSequence, clientId);
		Mockito.doNothing().when(clientRedisRepository).deleteById(Mockito.any(UUID.class));

		clientRedisProjectorAdapter.deleteById(event);
		
		Mockito.verify(clientRedisRepository, times(1)).deleteById(Mockito.any(UUID.class));
	}

	@Test
	void ClientRedisProjectorAdapter_existById_ReturnVoid() {
		
		Mockito.when(clientRedisRepository.existsById(Mockito.any(UUID.class))).thenReturn(true);

		boolean resultValue = clientRedisProjectorAdapter.existsById(clientId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(resultValue).isTrue()
				);
		Mockito.verify(clientRedisRepository, times(1)).existsById(Mockito.any(UUID.class));
	}
	
}
