package dev.ime.application.dispatch;


import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.handler.command.CreateSeatCommandHandler;
import dev.ime.application.handler.command.DeleteSeatCommandHandler;
import dev.ime.application.handler.command.UpdateSeatCommandHandler;
import dev.ime.application.usecase.command.DeleteSeatCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;

@ExtendWith(MockitoExtension.class)
class SeatCommandDispatcherTest {	

	@Mock
	private CreateSeatCommandHandler createCommandHandler;

	@Mock
	private UpdateSeatCommandHandler updateCommandHandler;

	@Mock
	private DeleteSeatCommandHandler deleteCommandHandler;

	@InjectMocks
	private SeatCommandDispatcher seatCommandDispatcher;
	
	private class CommandTest implements Command{}	

	@Test
	void SeatCommandDispatcher_getCommandHandler_ReturnDeleteByIdCommandHandler() {
		
		DeleteSeatCommand command = new DeleteSeatCommand(UUID.randomUUID());
		
		CommandHandler<Object> queryHandler = seatCommandDispatcher.getCommandHandler(command);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(queryHandler).isNotNull(),
				()-> Assertions.assertThat(queryHandler).isEqualTo(deleteCommandHandler)
				);		
	}
	
	@Test
	void SeatCommandDispatcher_getCommandHandler_ReturnIllegalArgumentException() {	
		
		CommandTest command = new CommandTest();
		
		Exception ex =  org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> seatCommandDispatcher.getCommandHandler(command));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);			
	}

}
