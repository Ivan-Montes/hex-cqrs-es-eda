package dev.ime.application.handler;

import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.GetAllClientQuery;
import dev.ime.application.usecase.GetByIdClientQuery;
import dev.ime.domain.model.Client;
import dev.ime.domain.port.outbound.ClientNoSqlReadRepositoryPort;

@ExtendWith(MockitoExtension.class)
class GetByIdClientQueryHandlerTest {

	@Mock
	private ClientNoSqlReadRepositoryPort clientNoSqlReadRepositoryPort;
	
	@InjectMocks
	private GetByIdClientQueryHandler getByIdClientQueryHandler;
	
	private GetByIdClientQuery getByIdClientQuery;
	private GetAllClientQuery getAllClientQuery;
	private Client clientTest;
	private final UUID id = UUID.randomUUID();
	private final String name = "Triss";
	private final String lastname = "Merigold";

	@BeforeEach
	private void createObjects() {
		
		getAllClientQuery = new GetAllClientQuery(1,1);
		getByIdClientQuery = new GetByIdClientQuery(id);
		
		clientTest = new Client();
		clientTest.setClientId(id);
		clientTest.setName(name);
		clientTest.setLastname(lastname);
		
	}	

	@Test
	void GetByIdClientQueryHandler_handle_ReturnOptClient() {
		
		Mockito.when(clientNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(clientTest));
		
		Optional<Client> optClient = getByIdClientQueryHandler.handle(getByIdClientQuery);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optClient).isNotNull(),
				()-> Assertions.assertThat(optClient).isNotEmpty(),
				()-> Assertions.assertThat(optClient.get().getClientId()).isEqualTo(id),
				()-> Assertions.assertThat(optClient.get().getName()).isEqualTo(name),
				()-> Assertions.assertThat(optClient.get().getLastname()).isEqualTo(lastname)
				);		
	}

	@Test
	void GetByIdClientQueryHandler_handle_ReturnIllegalArgumentException() {		
				
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> getByIdClientQueryHandler.handle(getAllClientQuery));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}

	@Test
	void GetByIdClientQueryHandler_handle_ReturnResourceNotFoundException() {
		
		Mockito.when(clientNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
	
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> getByIdClientQueryHandler.handle(getByIdClientQuery));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}
}
