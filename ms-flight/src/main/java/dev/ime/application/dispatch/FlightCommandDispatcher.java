package dev.ime.application.dispatch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.handler.CreateFlightCommandHandler;
import dev.ime.application.handler.DeleteFlightCommandHandler;
import dev.ime.application.handler.UpdateFlightCommandHandler;
import dev.ime.application.usecase.CreateFlightCommand;
import dev.ime.application.usecase.DeleteFlightCommand;
import dev.ime.application.usecase.UpdateFlightCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;

@Component
public class FlightCommandDispatcher {
	
	private final Map<Class<? extends Command>, CommandHandler<?>> commandHandlers  = new HashMap<>();

	public FlightCommandDispatcher(CreateFlightCommandHandler createCommandHandler,
			UpdateFlightCommandHandler updateCommandHandler, DeleteFlightCommandHandler deleteCommandHandler) {
		super();
		commandHandlers.put(CreateFlightCommand.class, createCommandHandler);
		commandHandlers.put(UpdateFlightCommand.class, updateCommandHandler);
		commandHandlers.put(DeleteFlightCommand.class, deleteCommandHandler);
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
