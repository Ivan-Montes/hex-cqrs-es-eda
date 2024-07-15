package dev.ime.application.dispatch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.handler.command.CreateSeatCommandHandler;
import dev.ime.application.handler.command.DeleteSeatCommandHandler;
import dev.ime.application.handler.command.UpdateSeatCommandHandler;
import dev.ime.application.usecase.command.CreateSeatCommand;
import dev.ime.application.usecase.command.DeleteSeatCommand;
import dev.ime.application.usecase.command.UpdateSeatCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;

@Component
public class SeatCommandDispatcher {
	
	private final Map<Class<? extends Command>, CommandHandler<?>> commandHandlers  = new HashMap<>();

	public SeatCommandDispatcher(CreateSeatCommandHandler createCommandHandler,
			UpdateSeatCommandHandler updateCommandHandler, DeleteSeatCommandHandler deleteCommandHandler) {
		super();
		commandHandlers.put(CreateSeatCommand.class, createCommandHandler);
		commandHandlers.put(UpdateSeatCommand.class, updateCommandHandler);
		commandHandlers.put(DeleteSeatCommand.class, deleteCommandHandler);
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
