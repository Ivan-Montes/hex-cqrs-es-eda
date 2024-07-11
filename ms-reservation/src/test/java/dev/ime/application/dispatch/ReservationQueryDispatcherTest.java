package dev.ime.application.dispatch;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.handler.GetAllReservationQueryHandler;
import dev.ime.application.handler.GetByIdReservationQueryHandler;
import dev.ime.application.usecase.GetAllReservationQuery;
import dev.ime.application.usecase.GetByIdReservationQuery;
import dev.ime.domain.model.Reservation;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;


@ExtendWith(MockitoExtension.class)
class ReservationQueryDispatcherTest {

	@Mock
	private GetAllReservationQueryHandler getAllQueryHandler;
	
	@Mock
	private GetByIdReservationQueryHandler getByIdQueryHandler;
	
	@InjectMocks
	private ReservationQueryDispatcher reservationQueryDispatcher;

	private class QueryTest implements Query{}
	

	@Test
	void ReservationQueryDispatcher_getQueryHandler_ReturnGetAllHandler() {
		
		GetAllReservationQuery query = new GetAllReservationQuery(0,1);
		
		QueryHandler<List<Reservation>> queryHandler = reservationQueryDispatcher.getQueryHandler(query);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(queryHandler).isNotNull(),
				()-> Assertions.assertThat(queryHandler).isEqualTo(getAllQueryHandler)
				);		
	}

	@Test
	void ReservationQueryDispatcher_getQueryHandler_ReturnGetByIdHandler() {
		
		GetByIdReservationQuery query = new GetByIdReservationQuery(UUID.randomUUID());
		
		QueryHandler<Optional<Reservation>> queryHandler = reservationQueryDispatcher.getQueryHandler(query);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(queryHandler).isNotNull(),
				()-> Assertions.assertThat(queryHandler).isEqualTo(getByIdQueryHandler)
				);		
	}

	@Test
	void ReservationQueryDispatcher_getQueryHandler_ReturnIllegalArgumentException() {	
				
		QueryTest query = new QueryTest();
		Exception ex =  org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> reservationQueryDispatcher.getQueryHandler(query));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}

}
