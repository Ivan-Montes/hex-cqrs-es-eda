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

import dev.ime.application.handler.GetAllClientQueryHandler;
import dev.ime.application.handler.GetByIdClientQueryHandler;
import dev.ime.application.usecase.GetAllClientQuery;
import dev.ime.application.usecase.GetByIdClientQuery;
import dev.ime.domain.model.Client;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@ExtendWith(MockitoExtension.class)
class ClientQueryDispatcherTest {

	@Mock
	private GetAllClientQueryHandler getAllQueryHandler;
	
	@Mock
	private GetByIdClientQueryHandler getByIdQueryHandler;
	
	@InjectMocks
	private ClientQueryDispatcher clientQueryDispatcher;
	
	private class QueryTest implements Query{}
	
	@Test
	void ClientQueryDispatcher_getQueryHandler_ReturnGetAllHandler() {
		
		GetAllClientQuery query = new GetAllClientQuery(1,1);
		
		QueryHandler<List<Client>> queryHandler = clientQueryDispatcher.getQueryHandler(query);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(queryHandler).isNotNull(),
				()-> Assertions.assertThat(queryHandler).isEqualTo(getAllQueryHandler)
				);		
	}

	@Test
	void ClientQueryDispatcher_getQueryHandler_ReturnGetByIdHandler() {
		
		GetByIdClientQuery query = new GetByIdClientQuery(UUID.randomUUID());
		
		QueryHandler<Optional<Client>> queryHandler = clientQueryDispatcher.getQueryHandler(query);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(queryHandler).isNotNull(),
				()-> Assertions.assertThat(queryHandler).isEqualTo(getByIdQueryHandler)
				);		
	}

	@Test
	void ClientQueryDispatcher_getQueryHandler_ReturnIllegalArgumentException() {	
				
		QueryTest query = new QueryTest();
		Exception ex =  org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> clientQueryDispatcher.getQueryHandler(query));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}

}
