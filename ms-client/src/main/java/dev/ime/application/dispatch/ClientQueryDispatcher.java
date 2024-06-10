package dev.ime.application.dispatch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.handler.GetAllClientQueryHandler;
import dev.ime.application.handler.GetByIdClientQueryHandler;
import dev.ime.application.usecase.GetAllClientQuery;
import dev.ime.application.usecase.GetByIdClientQuery;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;


@Component
public class ClientQueryDispatcher {

	private final Map<Class<? extends Query>, QueryHandler<?>> queryHandlers = new HashMap<>();

	public ClientQueryDispatcher(GetAllClientQueryHandler getAllQueryHandler, GetByIdClientQueryHandler getByIdQueryHandler) {
		super();
		queryHandlers.put(GetAllClientQuery.class, getAllQueryHandler);
		queryHandlers.put(GetByIdClientQuery.class, getByIdQueryHandler);
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
