package dev.ime.application.handler;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.usecase.GetAllClientQuery;
import dev.ime.application.usecase.GetByIdClientQuery;
import dev.ime.domain.model.Client;
import dev.ime.domain.port.outbound.ClientNoSqlReadRepositoryPort;

@ExtendWith(MockitoExtension.class)
class GetAllClientQueryHandlerTest {

	@Mock
	private ClientNoSqlReadRepositoryPort clientNoSqlReadRepositoryPort;

	@InjectMocks
	private GetAllClientQueryHandler getAllClientQueryHandler;
	
	private GetByIdClientQuery getByIdClientQuery;
	private GetAllClientQuery getAllClientQuery;
	private List<Client> clientList;
	private Client clientTest;
	private final UUID clientId = UUID.randomUUID();
	private final String name = "Triss";
	private final String lastname = "Merigold";
	
	@BeforeEach
	private void createObjects() {
		
		getAllClientQuery = new GetAllClientQuery(1,1);
		getByIdClientQuery = new GetByIdClientQuery(clientId);

		clientList = new ArrayList<>();
		
		clientTest = new Client();
		clientTest.setClientId(clientId);
		clientTest.setName(name);
		clientTest.setLastname(lastname);
		
	}	
	
	@Test
	void GetAllClientQueryHandler_handle_ReturnList() {
		
		clientList.add(clientTest);
		Mockito.when(clientNoSqlReadRepositoryPort.findAll(1,1)).thenReturn(clientList);
		
		List<Client> list = getAllClientQueryHandler.handle(getAllClientQuery);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getClientId()).isEqualTo(clientId),
				()-> Assertions.assertThat(list.get(0).getName()).isEqualTo(name),
				()-> Assertions.assertThat(list.get(0).getLastname()).isEqualTo(lastname)
				);		
	}

	@Test
	void GetAllClientQueryHandler_handle_ReturnIllegalArgumentException() {		
				
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> getAllClientQueryHandler.handle(getByIdClientQuery));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}
	
}
