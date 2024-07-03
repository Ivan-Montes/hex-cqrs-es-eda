package dev.ime.application.dispatch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.handler.GetAllReservationQueryHandler;
import dev.ime.application.handler.GetByIdReservationQueryHandler;
import dev.ime.application.usecase.GetAllReservationQuery;
import dev.ime.application.usecase.GetByIdReservationQuery;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@Component
public class ReservationQueryDispatcher {

	private final Map<Class<? extends Query>, QueryHandler<?>> queryHandlers = new HashMap<>();
	
	public ReservationQueryDispatcher(GetAllReservationQueryHandler getAllReservationQueryHandler, GetByIdReservationQueryHandler getByIdReservationQueryHandler) {
		super();
		queryHandlers.put(GetAllReservationQuery.class, getAllReservationQueryHandler);
		queryHandlers.put(GetByIdReservationQuery.class, getByIdReservationQueryHandler);
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
