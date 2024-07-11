package dev.ime.application.dispatch;


import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.handler.CreateReservationCommandHandler;
import dev.ime.application.handler.DeleteReservationCommandHandler;
import dev.ime.application.handler.UpdateReservationCommandHandler;
import dev.ime.application.usecase.DeleteReservationCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;


@ExtendWith(MockitoExtension.class)
class ReservationCommandDispatcherTest {

	@Mock
	private CreateReservationCommandHandler createCommandHandler;	

	@Mock
	private UpdateReservationCommandHandler updateCommandHandler;

	@Mock
	private DeleteReservationCommandHandler deleteCommandHandler;

	@InjectMocks
	private ReservationCommandDispatcher reservationCommandDispatcher;

	private class CommandTest implements Command{}	

	@Test
	void ReservationCommandDispatcher_getCommandHandler_ReturnDeleteByIdCommandHandler() {
		
		DeleteReservationCommand command = new DeleteReservationCommand(UUID.randomUUID());
		
		CommandHandler<Object> queryHandler = reservationCommandDispatcher.getCommandHandler(command);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(queryHandler).isNotNull(),
				()-> Assertions.assertThat(queryHandler).isEqualTo(deleteCommandHandler)
				);		
	}
	
	@Test
	void ReservationCommandDispatcher_getCommandHandler_ReturnIllegalArgumentException() {	
		
		CommandTest command = new CommandTest();
		
		Exception ex =  org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> reservationCommandDispatcher.getCommandHandler(command));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);			
	}	

}
