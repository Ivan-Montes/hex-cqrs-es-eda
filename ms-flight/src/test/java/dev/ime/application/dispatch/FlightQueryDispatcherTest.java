package dev.ime.application.dispatch;


import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.handler.GetAllFlightQueryHandler;
import dev.ime.application.handler.GetByIdFlightQueryHandler;
import dev.ime.application.usecase.GetByIdFlightQuery;
import dev.ime.domain.model.Flight;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@ExtendWith(MockitoExtension.class)
class FlightQueryDispatcherTest {
	
	@Mock
	private GetAllFlightQueryHandler getAllQueryHandler;
	
	@Mock
	private GetByIdFlightQueryHandler getByIdQueryHandler;

	@InjectMocks
	private FlightQueryDispatcher flightQueryDispatcher;

	private class QueryTest implements Query{}	
	
	@Test
	void FlightQueryDispatcher_getQueryHandler_ReturnGetByIdHandler() {
		
		GetByIdFlightQuery query = new GetByIdFlightQuery(UUID.randomUUID());
		
		QueryHandler<Optional<Flight>> queryHandler = flightQueryDispatcher.getQueryHandler(query);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(queryHandler).isNotNull(),
				()-> Assertions.assertThat(queryHandler).isEqualTo(getByIdQueryHandler)
				);		
	}

	@Test
	void FlightQueryDispatcher_getQueryHandler_ReturnIllegalArgumentException() {	
				
		QueryTest query = new QueryTest();
		Exception ex =  org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> flightQueryDispatcher.getQueryHandler(query));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}
	
}
