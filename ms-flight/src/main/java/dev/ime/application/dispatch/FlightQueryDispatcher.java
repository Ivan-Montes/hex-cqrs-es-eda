package dev.ime.application.dispatch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.handler.GetAllFlightQueryHandler;
import dev.ime.application.handler.GetByIdFlightQueryHandler;
import dev.ime.application.usecase.GetAllFlightQuery;
import dev.ime.application.usecase.GetByIdFlightQuery;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@Component
public class FlightQueryDispatcher {

	private final Map<Class<? extends Query>, QueryHandler<?>> queryHandlers = new HashMap<>();

	public FlightQueryDispatcher(GetAllFlightQueryHandler getAllQueryHandler, GetByIdFlightQueryHandler getByIdQueryHandler) {
		super();
		queryHandlers.put(GetAllFlightQuery.class, getAllQueryHandler);
		queryHandlers.put(GetByIdFlightQuery.class, getByIdQueryHandler);
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
