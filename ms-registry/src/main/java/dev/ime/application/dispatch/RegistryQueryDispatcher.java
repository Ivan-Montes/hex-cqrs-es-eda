package dev.ime.application.dispatch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.handler.GetAllRegistryQueryHandler;
import dev.ime.application.handler.GetByIdRegistryQueryHandler;
import dev.ime.application.usecase.GetAllRegistryQuery;
import dev.ime.application.usecase.GetByIdRegistryQuery;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@Component
public class RegistryQueryDispatcher {

	private final Map<Class<? extends Query>, QueryHandler<?>> queryHandlers = new HashMap<>();
	
	public RegistryQueryDispatcher(GetAllRegistryQueryHandler getAllRegistryQueryHandler, GetByIdRegistryQueryHandler getByIdRegistryQueryHandler) {
		super();
		queryHandlers.put(GetAllRegistryQuery.class, getAllRegistryQueryHandler);
		queryHandlers.put(GetByIdRegistryQuery.class, getByIdRegistryQueryHandler);
	}
	
	
	public <U> QueryHandler<U> getQueryHandler(Query query){

		@SuppressWarnings("unchecked")
		QueryHandler<U> handler = (QueryHandler<U>) queryHandlers.get(query.getClass());
		
		if ( handler == null ) {
			
			throw new IllegalArgumentException(ApplicationConstant.MSG_NO_HANDLER + query.getClass().getName());
	
		}
		
		return handler;
		
	}
}
