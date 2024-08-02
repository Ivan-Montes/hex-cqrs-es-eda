package dev.ime.application.dispatch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.handler.CreateRegistryCommandHandler;
import dev.ime.application.usecase.CreateRegistryCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;

@Component
public class RegistryCommandDispatcher {
	
	private final Map<Class<? extends Command>, CommandHandler<?>> commandHandlers  = new HashMap<>();	

	public RegistryCommandDispatcher(CreateRegistryCommandHandler createRegistryCommandHandler) {
		super();
		commandHandlers.put(CreateRegistryCommand.class, createRegistryCommandHandler);
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
