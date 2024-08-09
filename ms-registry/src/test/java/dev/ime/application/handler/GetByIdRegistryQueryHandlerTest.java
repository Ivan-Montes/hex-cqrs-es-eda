package dev.ime.application.handler;


import java.time.Instant;
import java.util.HashMap;
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
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.GetAllRegistryQuery;
import dev.ime.application.usecase.GetByIdRegistryQuery;
import dev.ime.domain.model.Registry;
import dev.ime.domain.port.outbound.GenericReadRepositoryPort;

@ExtendWith(MockitoExtension.class)
class GetByIdRegistryQueryHandlerTest {

	@Mock
	private GenericReadRepositoryPort<Registry> genericNoSqlReadRepositoryPort;

	@InjectMocks
	private GetByIdRegistryQueryHandler getByIdRegistryQueryHandler;
	
	private GetByIdRegistryQuery getByIdRegistryQuery;	
	private GetAllRegistryQuery getAllRegistryQuery;
	private final UUID registryId = UUID.randomUUID();
	private final Long sequence = 73L;
	private Registry registryTest;
	private Instant timeInstant;
	
	@BeforeEach
	private void createObjects() {
		
		getAllRegistryQuery = new GetAllRegistryQuery(0,1);
		getByIdRegistryQuery = new GetByIdRegistryQuery(registryId);
		timeInstant = Instant.now();		

		registryTest = new Registry();
		registryTest.setEventId(registryId);
		registryTest.setEventCategory(ApplicationConstant.CAT_REGISTRY);
		registryTest.setEventType(ApplicationConstant.REGISTRY_CREATED);
		registryTest.setTimeInstant(timeInstant);
		registryTest.setSequence(sequence);
		registryTest.setEventData(new HashMap<>());
		
	}
		
	
	@Test
	void GetByIdRegistryQueryHandler_handle_ReturnOpt() {
		
		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(registryTest));
		
		Optional<Registry> optRegistry = getByIdRegistryQueryHandler.handle(getByIdRegistryQuery);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optRegistry).isNotNull(),
				()-> Assertions.assertThat(optRegistry.get().getEventId()).isEqualTo(registryId)
				);
	}

	@Test
	void GetByIdRegistryQueryHandler_handle_ReturnIllegalArgumentException() {		
				
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> getByIdRegistryQueryHandler.handle(getAllRegistryQuery));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}

	@Test
	void GetByIdRegistryQueryHandler_handle_ReturnResourceNotFoundException() {
		
		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
	
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> getByIdRegistryQueryHandler.handle(getByIdRegistryQuery));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}	

}
