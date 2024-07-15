package dev.ime.application.dispatch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.handler.command.CreatePlaneCommandHandler;
import dev.ime.application.handler.command.DeletePlaneCommandHandler;
import dev.ime.application.handler.command.UpdatePlaneCommandHandler;
import dev.ime.application.usecase.command.CreatePlaneCommand;
import dev.ime.application.usecase.command.DeletePlaneCommand;
import dev.ime.application.usecase.command.UpdatePlaneCommand;
import dev.ime.domain.command.Command;
import dev.ime.domain.command.CommandHandler;

@Component
public class PlaneCommandDispatcher {
	
	private final Map<Class<? extends Command>, CommandHandler<?>> commandHandlers  = new HashMap<>();	
	
	public PlaneCommandDispatcher(CreatePlaneCommandHandler createPlaneCommandHandler, UpdatePlaneCommandHandler updatePlaneCommandHandler,
			DeletePlaneCommandHandler deletePlaneCommandHandler) {
		super();
		commandHandlers.put(CreatePlaneCommand.class, createPlaneCommandHandler);
		commandHandlers.put(UpdatePlaneCommand.class, updatePlaneCommandHandler);
		commandHandlers.put(DeletePlaneCommand.class, deletePlaneCommandHandler);
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
