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

import dev.ime.application.handler.GetAllRegistryQueryHandler;
import dev.ime.application.handler.GetByIdRegistryQueryHandler;
import dev.ime.application.usecase.GetAllRegistryQuery;
import dev.ime.application.usecase.GetByIdRegistryQuery;
import dev.ime.domain.model.Registry;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@ExtendWith(MockitoExtension.class)
class RegistryQueryDispatcherTest {

	@Mock
	private GetAllRegistryQueryHandler getAllQueryHandler;
	
	@Mock
	private GetByIdRegistryQueryHandler getByIdQueryHandler;
	
	@InjectMocks
	private RegistryQueryDispatcher registryQueryDispatcher;

	private class QueryTest implements Query{}
	

	@Test
	void RegistryQueryDispatcher_getQueryHandler_ReturnGetAllHandler() {
		
		GetAllRegistryQuery query = new GetAllRegistryQuery(0,1);
		
		QueryHandler<List<Registry>> queryHandler = registryQueryDispatcher.getQueryHandler(query);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(queryHandler).isNotNull(),
				()-> Assertions.assertThat(queryHandler).isEqualTo(getAllQueryHandler)
				);		
	}

	@Test
	void RegistryQueryDispatcher_getQueryHandler_ReturnGetByIdHandler() {
		
		GetByIdRegistryQuery query = new GetByIdRegistryQuery(UUID.randomUUID());
		
		QueryHandler<Optional<Registry>> queryHandler = registryQueryDispatcher.getQueryHandler(query);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(queryHandler).isNotNull(),
				()-> Assertions.assertThat(queryHandler).isEqualTo(getByIdQueryHandler)
				);		
	}

	@Test
	void RegistryQueryDispatcher_getQueryHandler_ReturnIllegalArgumentException() {	
				
		QueryTest query = new QueryTest();
		Exception ex =  org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> registryQueryDispatcher.getQueryHandler(query));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}


}
