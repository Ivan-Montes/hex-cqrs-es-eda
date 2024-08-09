package dev.ime.application.service;


import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
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

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.dispatch.RegistryQueryDispatcher;
import dev.ime.domain.model.Registry;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@ExtendWith(MockitoExtension.class)
class RegistryQueryServiceTest {

	@Mock
	private RegistryQueryDispatcher registryQueryDispatcher;

	@InjectMocks
	private RegistryQueryService registryQueryService;

	@Mock
	private QueryHandler<Object> queryHandler;
	
	private final UUID registryId = UUID.randomUUID();
	private final Long sequence = 73L;
	private Registry registryTest;
	private Instant timeInstant;
	private List<Registry> registryList;
	
	@BeforeEach
	private void createObjects() {
		
		timeInstant = Instant.now();		

		registryTest = new Registry(
				registryId,
				ApplicationConstant.CAT_REGISTRY,
				ApplicationConstant.REGISTRY_CREATED,
				timeInstant,
				sequence,
				new HashMap<>()
				);
		registryList  = new ArrayList<>();
		
	}
	
	@Test
	void RegistryQueryService_getAll_ReturnListRegistry() {

		registryList.add(registryTest);
		Mockito.when(registryQueryDispatcher.getQueryHandler(Mockito.any(Query.class))).thenReturn(queryHandler);
		Mockito.when(queryHandler.handle(Mockito.any(Query.class))).thenReturn(registryList);
		
		List<Registry> list = registryQueryService.getAll(0,1);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getEventId()).isEqualTo(registryId),
				()-> Assertions.assertThat(list.get(0).getEventCategory()).isEqualTo(ApplicationConstant.CAT_REGISTRY),
				()-> Assertions.assertThat(list.get(0).getEventType()).isEqualTo(ApplicationConstant.REGISTRY_CREATED),
				()-> Assertions.assertThat(list.get(0).getTimeInstant()).isEqualTo(timeInstant),
				()-> Assertions.assertThat(list.get(0).getSequence()).isEqualTo(sequence)
				);
	
	}

	@Test
	void RegistryQueryService_getById_ReturnOptionalRegistry() {

		Mockito.when(registryQueryDispatcher.getQueryHandler(Mockito.any(Query.class))).thenReturn(queryHandler);
		Mockito.when(queryHandler.handle(Mockito.any(Query.class))).thenReturn(Optional.ofNullable(registryTest));
		
		Optional<Registry> optRegistry = registryQueryService.getById(registryId);
				
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optRegistry).isNotNull(),
				()-> Assertions.assertThat(optRegistry.get().toString()).hasToString(registryTest.toString()),
				()-> Assertions.assertThat(optRegistry.get().hashCode()).hasSameHashCodeAs(registryTest.hashCode()),
				()-> Assertions.assertThat(optRegistry).contains(registryTest)
				);
	}	
	
}
