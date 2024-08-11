package dev.ime.infrastructure.adapter;

import static org.mockito.Mockito.times;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.ime.application.event.RegistryCreatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.infrastructure.entity.RegistryJpaEntity;
import dev.ime.infrastructure.repository.RegistryJpaRepository;

@ExtendWith(MockitoExtension.class)
class RegistryProjectorAdapterTest {

	@Mock
	private LoggerUtil loggerUtil;
	
	@Mock
	private RegistryJpaRepository registryJpaRepository;
	
	@Mock
	private ObjectMapper objectMapper;
	
	@InjectMocks
	private RegistryProjectorAdapter registryProjectorAdapter;
	
	private class JsonModException extends JsonProcessingException{

		private static final long serialVersionUID = -3841997292425435734L;

		protected JsonModException(String msg) {
			super(msg);
		}};
	
	@Test
	void RegistryProjectorAdapter_create_ReturnVoid() throws JsonProcessingException {
		
		RegistryCreatedEvent event = new RegistryCreatedEvent(
				37L,
				new HashMap<>()
				);
		Mockito.when(objectMapper.writeValueAsString(Mockito.any())).thenReturn("");
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.when(registryJpaRepository.save(Mockito.any(RegistryJpaEntity.class))).thenReturn(new RegistryJpaEntity());
				
		registryProjectorAdapter.create(event);
		
		Mockito.verify(objectMapper, times(1)).writeValueAsString(Mockito.any());
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(registryJpaRepository, times(1)).save(Mockito.any(RegistryJpaEntity.class));
	}

	@Test
	void RegistryProjectorAdapter_create_ReturnVoidWithException() throws JsonProcessingException {
		
		RegistryCreatedEvent event = new RegistryCreatedEvent(
				37L,
				new HashMap<>()
				);
		Mockito.when(objectMapper.writeValueAsString(Mockito.any())).thenThrow(new JsonModException("Excelsior"));
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.when(registryJpaRepository.save(Mockito.any(RegistryJpaEntity.class))).thenReturn(new RegistryJpaEntity());
				
		registryProjectorAdapter.create(event);
		
		Mockito.verify(objectMapper, times(1)).writeValueAsString(Mockito.any());
		Mockito.verify(loggerUtil, times(2)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(registryJpaRepository, times(1)).save(Mockito.any(RegistryJpaEntity.class));
	}

}
