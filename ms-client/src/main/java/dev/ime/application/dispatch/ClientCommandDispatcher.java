package dev.ime.application.dispatch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.handler.CreateClientCommandHandler;
import dev.ime.application.handler.UpdateClientCommandHandler;
import dev.ime.application.handler.DeleteClientCommandHandler;
import dev.ime.application.usecase.CreateClientCommand;
import dev.ime.application.usecase.DeleteClientCommand;
import dev.ime.application.usecase.UpdateClientCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;

@Component
public class ClientCommandDispatcher {

	private final Map<Class<? extends Command>, CommandHandler<?>> commandHandlers  = new HashMap<>();

	public ClientCommandDispatcher(CreateClientCommandHandler createCommandHandler,
			UpdateClientCommandHandler updateCommandHandler, DeleteClientCommandHandler deleteCommandHandler) {
		super();
		commandHandlers.put(CreateClientCommand.class, createCommandHandler);
		commandHandlers.put(UpdateClientCommand.class, updateCommandHandler);
		commandHandlers.put(DeleteClientCommand.class, deleteCommandHandler);
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
