package dev.ime.application.dispatch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.handler.query.GetAllPlaneQueryHandler;
import dev.ime.application.handler.query.GetByIdPlaneQueryHandler;
import dev.ime.application.usecase.query.GetAllPlaneQuery;
import dev.ime.application.usecase.query.GetByIdPlaneQuery;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@Component
public class PlaneQueryDispatcher {
	
	private final Map<Class<? extends Query>, QueryHandler<?>> queryHandlers = new HashMap<>();

	public PlaneQueryDispatcher(GetAllPlaneQueryHandler getAllQueryHandler, GetByIdPlaneQueryHandler getByIdQueryHandler) {
		super();
		queryHandlers.put(GetAllPlaneQuery.class, getAllQueryHandler);
		queryHandlers.put(GetByIdPlaneQuery.class, getByIdQueryHandler);
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
