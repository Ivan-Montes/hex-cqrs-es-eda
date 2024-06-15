package dev.ime.infrastructure.adapter;

import static org.mockito.Mockito.times;

import java.util.Optional;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.event.ClientCreatedEvent;
import dev.ime.application.event.ClientDeletedEvent;
import dev.ime.application.event.ClientUpdatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.infrastructure.entity.ClientMongoEntity;
import dev.ime.infrastructure.repository.read.ClientNoSqlReadRepository;

@ExtendWith(MockitoExtension.class)
class ClientProjectorAdapterTest {

	@Mock
	private ClientNoSqlReadRepository clientNoSqlReadRepository;

	@Mock
	private LoggerUtil loggerUtil;

	@InjectMocks
	private ClientProjectorAdapter clientProjectorAdapter;

	private ClientMongoEntity mongoEntity;
	private final ObjectId objectId = ObjectId.get();
	private final UUID clientId = UUID.randomUUID();
	private final String name = "Triss";
	private final String lastname = "Merigold";
	private final Long databaseSequence = 9L;
	
	@BeforeEach
	private void createObjects() {
		
		mongoEntity = new ClientMongoEntity(
				objectId,
				clientId,
				name,
				lastname);
				
	}	
	
	@Test
	void ClientProjectorAdapter_create_ReturnVoid() {
		
		ClientCreatedEvent event = new ClientCreatedEvent(databaseSequence, clientId, name, lastname);
		Mockito.when(clientNoSqlReadRepository.save(Mockito.any(ClientMongoEntity.class))).thenReturn(mongoEntity);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		clientProjectorAdapter.create(event);
		
		Mockito.verify(clientNoSqlReadRepository, times(1)).save(Mockito.any(ClientMongoEntity.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}

	@Test
	void ClientProjectorAdapter_update_ReturnVoid() {
		
		ClientUpdatedEvent event = new ClientUpdatedEvent(databaseSequence, clientId, name, lastname);
		Mockito.when(clientNoSqlReadRepository.findFirstByClientId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(mongoEntity));
		Mockito.when(clientNoSqlReadRepository.save(Mockito.any(ClientMongoEntity.class))).thenReturn(mongoEntity);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		clientProjectorAdapter.update(event);
		
		Mockito.verify(clientNoSqlReadRepository, times(1)).findFirstByClientId(Mockito.any(UUID.class));
		Mockito.verify(clientNoSqlReadRepository, times(1)).save(Mockito.any(ClientMongoEntity.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}

	@Test
	void ClientProjectorAdapter_update_ReturnVoidByOptEmpty() {
		
		ClientUpdatedEvent event = new ClientUpdatedEvent(databaseSequence, clientId, name, lastname);
		Mockito.when(clientNoSqlReadRepository.findFirstByClientId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		clientProjectorAdapter.update(event);
		
		Mockito.verify(clientNoSqlReadRepository, times(1)).findFirstByClientId(Mockito.any(UUID.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}

	@Test
	void ClientProjectorAdapter_deleteById_ReturnVoid() {
		
		ClientDeletedEvent event = new ClientDeletedEvent(databaseSequence, clientId);
		Mockito.when(clientNoSqlReadRepository.findFirstByClientId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(mongoEntity));
		Mockito.doNothing().when(clientNoSqlReadRepository).delete(Mockito.any(ClientMongoEntity.class));
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		clientProjectorAdapter.deleteById(event);
		
		Mockito.verify(clientNoSqlReadRepository, times(1)).findFirstByClientId(Mockito.any(UUID.class));
		Mockito.verify(clientNoSqlReadRepository, times(1)).delete(Mockito.any(ClientMongoEntity.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}

	@Test
	void ClientProjectorAdapter_deleteById_ReturnVoidByOptEmpty() {
		
		ClientDeletedEvent event = new ClientDeletedEvent(databaseSequence, clientId);
		Mockito.when(clientNoSqlReadRepository.findFirstByClientId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		clientProjectorAdapter.deleteById(event);
		
		Mockito.verify(clientNoSqlReadRepository, times(1)).findFirstByClientId(Mockito.any(UUID.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}
	

}
