package dev.ime.application.dispatch;


import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.handler.command.CreatePlaneCommandHandler;
import dev.ime.application.handler.command.DeletePlaneCommandHandler;
import dev.ime.application.handler.command.UpdatePlaneCommandHandler;
import dev.ime.application.usecase.command.DeletePlaneCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;

@ExtendWith(MockitoExtension.class)
class PlaneCommandDispatcherTest {

	@Mock
	private CreatePlaneCommandHandler createPlaneCommandHandler;

	@Mock
	private UpdatePlaneCommandHandler updatePlaneCommandHandler;

	@Mock
	private DeletePlaneCommandHandler deletePlaneCommandHandler;

	@InjectMocks
	private PlaneCommandDispatcher planeCommandDispatcher;
	
	private class CommandTest implements Command{}

	@Test
	void PlaneCommandDispatcher_getCommandHandler_ReturnDeleteByIdCommandHandler() {
		
		DeletePlaneCommand command = new DeletePlaneCommand(UUID.randomUUID());
		
		CommandHandler<Object> queryHandler = planeCommandDispatcher.getCommandHandler(command);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(queryHandler).isNotNull(),
				()-> Assertions.assertThat(queryHandler).isEqualTo(deletePlaneCommandHandler)
				);		
	}
	
	@Test
	void PlaneCommandDispatcher_getCommandHandler_ReturnIllegalArgumentException() {	
		
		CommandTest command = new CommandTest();
		
		Exception ex =  org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> planeCommandDispatcher.getCommandHandler(command));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);			
	}

}
