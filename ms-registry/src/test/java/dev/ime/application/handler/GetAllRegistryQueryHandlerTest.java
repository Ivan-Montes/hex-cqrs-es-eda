package dev.ime.application.handler;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.usecase.GetAllRegistryQuery;
import dev.ime.application.usecase.GetByIdRegistryQuery;
import dev.ime.domain.model.Registry;
import dev.ime.domain.port.outbound.GenericReadRepositoryPort;

@ExtendWith(MockitoExtension.class)
class GetAllRegistryQueryHandlerTest {

	@Mock
	private GenericReadRepositoryPort<Registry> genericNoSqlReadRepositoryPort;

	@InjectMocks
	private GetAllRegistryQueryHandler getAllRegistryQueryHandler;

	@Test
	void GetAllRegistryQueryHandler_handle_ReturnList() {
		
		List<Registry>registryList = new ArrayList<>();
		registryList.add(new Registry());
		Mockito.when(genericNoSqlReadRepositoryPort.findAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(registryList);

		List<Registry> list = getAllRegistryQueryHandler.handle(new GetAllRegistryQuery(0,1));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1)
				);		
	}

	@Test
	void GetAllRegistryQueryHandler_handle_ReturnIllegalArgumentException() {	
		
		GetByIdRegistryQuery query = new GetByIdRegistryQuery(UUID.randomUUID());
		
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> getAllRegistryQueryHandler.handle(query));
		
		org.junit.jupiter.api.Assertions.assertAll(
		()-> Assertions.assertThat(ex).isNotNull(),
		()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
		);		

	}
	
}
