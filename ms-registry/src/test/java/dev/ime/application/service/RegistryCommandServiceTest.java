package dev.ime.application.service;


import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.ime.application.dispatch.RegistryCommandDispatcher;
import dev.ime.application.event.RegistryCreatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.PublisherPort;

@ExtendWith(MockitoExtension.class)
class RegistryCommandServiceTest {

	@Mock
	private RegistryCommandDispatcher registryCommandDispatcher;
	@Mock
	private PublisherPort publisherPort;
	@Mock
	private ObjectMapper objectMapper;
	@Mock
	private LoggerUtil loggerUtil;

	@InjectMocks
	private RegistryCommandService registryCommandService;

	@Mock
	private CommandHandler<Optional<Event>> commandHandler;

	private Long sequence = 73L;
	private Map<String, Object> eventDataMap = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	@Test
	void RegistryCommandService_create_ReturnVoid() {
		
		RegistryCreatedEvent event = new RegistryCreatedEvent(
				sequence,
				new HashMap<>()
				);
		Mockito.when(objectMapper.convertValue(Mockito.any(Event.class), Mockito.any(TypeReference.class))).thenReturn(eventDataMap);
		doReturn(commandHandler).when(registryCommandDispatcher).getCommandHandler(Mockito.any(Command.class));
		Mockito.when(commandHandler.handle(Mockito.any(Command.class))).thenReturn(Optional.ofNullable(event));
		
		registryCommandService.create(event);
		
		Mockito.verify(objectMapper, times(1)).convertValue(Mockito.any(Event.class), Mockito.any(TypeReference.class));
		Mockito.verify(registryCommandDispatcher, times(1)).getCommandHandler(Mockito.any(Command.class));
		Mockito.verify(publisherPort, times(1)).publishEvent(Mockito.any(Event.class));
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}

}
