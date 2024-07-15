package dev.ime.application.dispatch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.handler.query.GetAllSeatQueryHandler;
import dev.ime.application.handler.query.GetByIdSeatQueryHandler;
import dev.ime.application.usecase.query.GetAllSeatQuery;
import dev.ime.application.usecase.query.GetByIdSeatQuery;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@Component
public class SeatQueryDispatcher {
	
	private final Map<Class<? extends Query>, QueryHandler<?>> queryHandlers = new HashMap<>();

	public SeatQueryDispatcher(GetAllSeatQueryHandler getAllQueryHandler, GetByIdSeatQueryHandler getByIdQueryHandler) {
		super();
		queryHandlers.put(GetAllSeatQuery.class, getAllQueryHandler);
		queryHandlers.put(GetByIdSeatQuery.class, getByIdQueryHandler);
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
