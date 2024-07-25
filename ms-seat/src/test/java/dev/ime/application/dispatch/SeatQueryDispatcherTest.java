package dev.ime.application.dispatch;


import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.handler.query.GetAllSeatQueryHandler;
import dev.ime.application.handler.query.GetByIdSeatQueryHandler;
import dev.ime.application.usecase.query.GetByIdSeatQuery;
import dev.ime.domain.model.Seat;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@ExtendWith(MockitoExtension.class)
class SeatQueryDispatcherTest {

	@Mock
	private GetAllSeatQueryHandler getAllQueryHandler;

	@Mock
	private GetByIdSeatQueryHandler getByIdQueryHandler;
	
	@InjectMocks
	private SeatQueryDispatcher seatQueryDispatcher;

	private class QueryTest implements Query{}	

	@Test
	void SeatQueryDispatcher_getQueryHandler_ReturnGetByIdHandler() {
		
		GetByIdSeatQuery query = new GetByIdSeatQuery(UUID.randomUUID());
		
		QueryHandler<Optional<Seat>> queryHandler = seatQueryDispatcher.getQueryHandler(query);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(queryHandler).isNotNull(),
				()-> Assertions.assertThat(queryHandler).isEqualTo(getByIdQueryHandler)
				);		
	}

	@Test
	void SeatQueryDispatcher_getQueryHandler_ReturnIllegalArgumentException() {	
				
		QueryTest query = new QueryTest();
		Exception ex =  org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> seatQueryDispatcher.getQueryHandler(query));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}

}
