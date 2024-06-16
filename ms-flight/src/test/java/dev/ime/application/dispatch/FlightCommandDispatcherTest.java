package dev.ime.application.dispatch;


import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.handler.CreateFlightCommandHandler;
import dev.ime.application.handler.DeleteFlightCommandHandler;
import dev.ime.application.handler.UpdateFlightCommandHandler;
import dev.ime.application.usecase.DeleteFlightCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;

@ExtendWith(MockitoExtension.class)
class FlightCommandDispatcherTest {

	@Mock
	private CreateFlightCommandHandler createCommandHandler;

	@Mock
	private UpdateFlightCommandHandler updateCommandHandler;

	@Mock
	private DeleteFlightCommandHandler deleteCommandHandler;
	
	@InjectMocks
	private FlightCommandDispatcher flightCommandDispatcher;
	
	private class CommandTest implements Command{}	
	
	@Test
	void FlightCommandDispatcher_getCommandHandler_ReturnDeleteByIdCommandHandler() {
		
		DeleteFlightCommand command = new DeleteFlightCommand(UUID.randomUUID());
		
		CommandHandler<Object> queryHandler = flightCommandDispatcher.getCommandHandler(command);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(queryHandler).isNotNull(),
				()-> Assertions.assertThat(queryHandler).isEqualTo(deleteCommandHandler)
				);		
	}
	
	@Test
	void FlightCommandDispatcher_getCommandHandler_ReturnIllegalArgumentException() {	
		
		CommandTest command = new CommandTest();
		
		Exception ex =  org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> flightCommandDispatcher.getCommandHandler(command));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);			
	}

}
