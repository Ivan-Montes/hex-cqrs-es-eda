package dev.ime.application.dispatch;


import java.util.HashMap;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.handler.CreateRegistryCommandHandler;
import dev.ime.application.usecase.CreateRegistryCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;

@ExtendWith(MockitoExtension.class)
class RegistryCommandDispatcherTest {

	@Mock
	private CreateRegistryCommandHandler createRegistryCommandHandler;

	@InjectMocks
	private RegistryCommandDispatcher registryCommandDispatcher;

	private class CommandTest implements Command{}	

	@Test
	void RegistryCommandDispatcher_getCommandHandler_ReturnHandler() {
		
		CreateRegistryCommand command = new CreateRegistryCommand(
				UUID.randomUUID(),
				new HashMap<>()
				);
		
		CommandHandler<Object> queryHandler = registryCommandDispatcher.getCommandHandler(command);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(queryHandler).isNotNull(),
				()-> Assertions.assertThat(queryHandler).isEqualTo(createRegistryCommandHandler)
				);	
	}

	@Test
	void RegistryCommandDispatcher_getCommandHandler_ReturnIllegalArgumentException() {	
		
		CommandTest command = new CommandTest();
		
		Exception ex =  org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> registryCommandDispatcher.getCommandHandler(command));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);			
	}
	
}
