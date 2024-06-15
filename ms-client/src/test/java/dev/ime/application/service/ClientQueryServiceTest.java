package dev.ime.application.service;


import java.util.ArrayList;
import java.util.List;
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

import dev.ime.application.dispatch.ClientQueryDispatcher;
import dev.ime.domain.model.Client;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@ExtendWith(MockitoExtension.class)
class ClientQueryServiceTest {

	@Mock
	private ClientQueryDispatcher clientQueryDispatcher;
	
	@InjectMocks
	private ClientQueryService clientQueryService;
	
	@Mock
	private QueryHandler<Object> queryHandler;
	
	private List<Client> clientList;
	private Client clientTest;
	private final UUID clientId = UUID.randomUUID();
	private final String name = "Triss";
	private final String lastname = "Merigold";
	
	@BeforeEach
	private void createObjects() {
		
		clientList = new ArrayList<>();
		clientTest = new Client(clientId, name, lastname);
		
	}
	
	@Test
	void ClientQueryService_getAll_ReturnListClient() {
		
		clientList.add(clientTest);
		Mockito.when(clientQueryDispatcher.getQueryHandler(Mockito.any(Query.class))).thenReturn(queryHandler);
		Mockito.when(queryHandler.handle(Mockito.any(Query.class))).thenReturn(clientList);
		
		List<Client> list = clientQueryService.getAll();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getClientId()).isEqualTo(clientId),
				()-> Assertions.assertThat(list.get(0).getName()).isEqualTo(name),
				()-> Assertions.assertThat(list.get(0).getLastname()).isEqualTo(lastname)
				);		
	}

	@Test
	void ClientQueryService_getById_ReturnOptionalClient() {
		
		Mockito.when(clientQueryDispatcher.getQueryHandler(Mockito.any(Query.class))).thenReturn(queryHandler);
		Mockito.when(queryHandler.handle(Mockito.any(Query.class))).thenReturn(Optional.ofNullable(clientTest));

		Optional<Client> optClient = clientQueryService.getById(clientId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optClient).isNotNull(),
				()-> Assertions.assertThat(optClient).isNotEmpty(),
				()-> Assertions.assertThat(optClient.get().getClientId()).isEqualTo(clientId),
				()-> Assertions.assertThat(optClient.get().getName()).isEqualTo(name),
				()-> Assertions.assertThat(optClient.get().getLastname()).isEqualTo(lastname),
				()-> Assertions.assertThat(optClient.get().toString()).hasToString(clientTest.toString()),
				()-> Assertions.assertThat(optClient.get().hashCode()).hasSameHashCodeAs(clientTest.hashCode()),
				()-> Assertions.assertThat(optClient).contains(clientTest)
				);	
	}

}
