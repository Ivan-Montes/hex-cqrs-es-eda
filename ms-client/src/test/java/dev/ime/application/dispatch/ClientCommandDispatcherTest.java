package dev.ime.application.dispatch;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.handler.CreateClientCommandHandler;
import dev.ime.application.handler.DeleteClientCommandHandler;
import dev.ime.application.handler.UpdateClientCommandHandler;
import dev.ime.application.usecase.DeleteClientCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;

@ExtendWith(MockitoExtension.class)
class ClientCommandDispatcherTest {

	@Mock
	private CreateClientCommandHandler createCommandHandler;
	
	@Mock
	private UpdateClientCommandHandler updateCommandHandler;
	
	@Mock
	private DeleteClientCommandHandler deleteCommandHandler;
	
	@InjectMocks
	private ClientCommandDispatcher clientCommandDispatcher;
	
	private class CommandTest implements Command{}
	
	@Test
	void ClientCommandDispatcher_getCommandHandler_ReturnDeleteByIdCommandHandler() {
		
		DeleteClientCommand command = new DeleteClientCommand(UUID.randomUUID());
		
		CommandHandler<Object> queryHandler = clientCommandDispatcher.getCommandHandler(command);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(queryHandler).isNotNull(),
				()-> Assertions.assertThat(queryHandler).isEqualTo(deleteCommandHandler)
				);		
	}
	
	@Test
	void ClientCommandDispatcher_getCommandHandler_ReturnIllegalArgumentException() {	
		
		CommandTest command = new CommandTest();
		
		Exception ex =  org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> clientCommandDispatcher.getCommandHandler(command));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);			
	}
	
}
