package dev.ime.application.handler.query;


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
import dev.ime.application.usecase.query.GetAllPlaneQuery;
import dev.ime.application.usecase.query.GetByIdPlaneQuery;
import dev.ime.domain.model.Plane;
import dev.ime.domain.port.outbound.GenericNoSqlReadRepositoryPort;

@ExtendWith(MockitoExtension.class)
class GetByIdPlaneQueryHandlerTest {

	@Mock
	private GenericNoSqlReadRepositoryPort<Plane> genericNoSqlReadRepositoryPort;

	@InjectMocks
	private GetByIdPlaneQueryHandler getByIdPlaneQueryHandler;

	private GetAllPlaneQuery getAllPlaneQuery;
	private GetByIdPlaneQuery getByIdPlaneQuery;
	private Plane planeTest;
	private final UUID planeId = UUID.randomUUID();
	private final String name = "Pursuit Special";
	private final Integer capacity = 2;
	
	
	@BeforeEach
	private void createObjects() {
		
		getAllPlaneQuery = new GetAllPlaneQuery(0,1);
		getByIdPlaneQuery = new GetByIdPlaneQuery(planeId);	
		
		planeTest = new Plane();
		planeTest.setPlaneId(planeId);
		planeTest.setName(name);
		planeTest.setCapacity(capacity);
	
	}	

	@Test
	void GetByIdPlaneQueryHandler_handle_ReturnOptionalPlane() {		

		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(planeTest));

		Optional<Plane>  optPlane = getByIdPlaneQueryHandler.handle(getByIdPlaneQuery);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optPlane).isNotNull(),
				()-> Assertions.assertThat(optPlane.get().getPlaneId()).isEqualTo(planeId),
				()-> Assertions.assertThat(optPlane.get().getName()).isEqualTo(name),
				()-> Assertions.assertThat(optPlane.get().getCapacity()).isEqualTo(capacity.intValue())
				);	

	}	

	@Test
	void GetByIdPlanetQueryHandler_handle_ReturnIllegalArgumentException() {		
				
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> getByIdPlaneQueryHandler.handle(getAllPlaneQuery));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}	

	@Test
	void GetByIdPlaneQueryHandler_handle_ReturnResourceNotFoundException() {
		
		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
	
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> getByIdPlaneQueryHandler.handle(getByIdPlaneQuery));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}

}
