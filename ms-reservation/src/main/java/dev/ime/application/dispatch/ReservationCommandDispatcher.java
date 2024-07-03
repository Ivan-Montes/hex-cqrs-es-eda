package dev.ime.application.dispatch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.handler.CreateReservationCommandHandler;
import dev.ime.application.handler.DeleteReservationCommandHandler;
import dev.ime.application.handler.UpdateReservationCommandHandler;
import dev.ime.application.usecase.CreateReservationCommand;
import dev.ime.application.usecase.DeleteReservationCommand;
import dev.ime.application.usecase.UpdateReservationCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;

@Component
public class ReservationCommandDispatcher {
	
	private final Map<Class<? extends Command>, CommandHandler<?>> commandHandlers  = new HashMap<>();	

	public ReservationCommandDispatcher(CreateReservationCommandHandler createReservationCommandHandler, UpdateReservationCommandHandler updateReservationCommandHandler, DeleteReservationCommandHandler deleteReservationCommandHandler) {
		super();
		commandHandlers.put(CreateReservationCommand.class, createReservationCommandHandler);
		commandHandlers.put(UpdateReservationCommand.class, updateReservationCommandHandler);
		commandHandlers.put(DeleteReservationCommand.class, deleteReservationCommandHandler);
	}

	public <U> CommandHandler<U> getCommandHandler(Command command) {

		@SuppressWarnings("unchecked")
		CommandHandler<U> handler = (CommandHandler<U>) commandHandlers.get(command.getClass());
		
		if (handler == null) {
			
			throw new IllegalArgumentException(ApplicationConstant.MSG_NO_HANDLER + command.getClass().getName());
		
		} 
		
		return handler;		
		
	}
	
}
