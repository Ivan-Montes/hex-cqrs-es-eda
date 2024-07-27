package dev.ime.application.handler.query;


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

import dev.ime.application.usecase.query.GetAllPlaneQuery;
import dev.ime.application.usecase.query.GetByIdPlaneQuery;
import dev.ime.domain.model.Plane;
import dev.ime.domain.port.outbound.GenericNoSqlReadRepositoryPort;

@ExtendWith(MockitoExtension.class)
class GetAllPlaneQueryHandlerTest {

	@Mock
	private GenericNoSqlReadRepositoryPort<Plane> genericNoSqlReadRepositoryPort;

	@InjectMocks
	private GetAllPlaneQueryHandler getAllPlaneQueryHandler;

	private GetAllPlaneQuery getAllPlaneQuery;
	private GetByIdPlaneQuery getByIdPlaneQuery;
	private List<Plane> planeList;
	private Plane planeTest;
	private final UUID planeId = UUID.randomUUID();
	private final String name = "Pursuit Special";
	private final Integer capacity = 2;
	
	
	@BeforeEach
	private void createObjects() {
		
		getAllPlaneQuery = new GetAllPlaneQuery(0,1);
		getByIdPlaneQuery = new GetByIdPlaneQuery(planeId);
	
		planeList = new ArrayList<>();
		
		planeTest = new Plane();
		planeTest.setPlaneId(planeId);
		planeTest.setName(name);
		planeTest.setCapacity(capacity);
	
	}	

	@Test
	void GetAllPlaneQueryHandler_handle_ReturnList() {
		
		planeList.add(planeTest);
		Mockito.when(genericNoSqlReadRepositoryPort.findAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(planeList);

		List<Plane> list = getAllPlaneQueryHandler.handle(getAllPlaneQuery);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getPlaneId()).isEqualTo(planeId),
				()-> Assertions.assertThat(list.get(0).getName()).isEqualTo(name),
				()-> Assertions.assertThat(list.get(0).getCapacity()).isEqualTo(capacity.intValue())
				);	
	}

	@Test
	void GetAllPlaneQueryHandler_handle_ReturnIllegalArgumentException() {		
				
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> getAllPlaneQueryHandler.handle(getByIdPlaneQuery));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}
}
