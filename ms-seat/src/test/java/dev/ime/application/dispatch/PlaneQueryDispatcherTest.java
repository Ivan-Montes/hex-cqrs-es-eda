package dev.ime.application.dispatch;


import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.handler.query.GetAllPlaneQueryHandler;
import dev.ime.application.handler.query.GetByIdPlaneQueryHandler;
import dev.ime.application.usecase.query.GetByIdPlaneQuery;
import dev.ime.domain.model.Plane;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@ExtendWith(MockitoExtension.class)
class PlaneQueryDispatcherTest {

	@Mock
	private GetAllPlaneQueryHandler getAllQueryHandler;

	@Mock
	private GetByIdPlaneQueryHandler getByIdQueryHandler;
	
	@InjectMocks
	private PlaneQueryDispatcher planeQueryDispatcher;

	private class QueryTest implements Query{}	

	@Test
	void PlaneQueryDispatcher_getQueryHandler_ReturnGetByIdHandler() {
		
		GetByIdPlaneQuery query = new GetByIdPlaneQuery(UUID.randomUUID());
		
		QueryHandler<Optional<Plane>> queryHandler = planeQueryDispatcher.getQueryHandler(query);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(queryHandler).isNotNull(),
				()-> Assertions.assertThat(queryHandler).isEqualTo(getByIdQueryHandler)
				);		
	}

	@Test
	void PlaneQueryDispatcher_getQueryHandler_ReturnIllegalArgumentException() {	
				
		QueryTest query = new QueryTest();
		Exception ex =  org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> planeQueryDispatcher.getQueryHandler(query));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}

}
