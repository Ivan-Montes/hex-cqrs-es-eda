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

import dev.ime.application.usecase.query.GetAllSeatQuery;
import dev.ime.application.usecase.query.GetByIdSeatQuery;
import dev.ime.domain.model.Plane;
import dev.ime.domain.model.Seat;
import dev.ime.domain.port.outbound.GenericNoSqlReadRepositoryPort;

@ExtendWith(MockitoExtension.class)
class GetAllSeatQueryHandlerTest {

	@Mock
	private GenericNoSqlReadRepositoryPort<Seat> genericNoSqlReadRepositoryPort;

	@InjectMocks
	private GetAllSeatQueryHandler getAllSeatQueryHandler;
	
	private GetAllSeatQuery getAllSeatQuery;
	private GetByIdSeatQuery getByIdSeatQuery;
	private List<Seat> seatList;
	private Seat seatTest;
	private Plane planeTest;
	private final UUID seatId = UUID.randomUUID();
	private final String seatNumber = "PS1973";
	private final UUID planeId = UUID.randomUUID();

	@BeforeEach
	private void createObjects() {
		
		getAllSeatQuery = new GetAllSeatQuery(0,1);
		getByIdSeatQuery = new GetByIdSeatQuery(seatId);
		
		seatList = new ArrayList<>();
		
		planeTest = new Plane();
		planeTest.setPlaneId(planeId);
		
		seatTest = new Seat();
		seatTest.setSeatId(seatId);
		seatTest.setSeatNumber(seatNumber);
		seatTest.setPlane(planeTest);
	}
	
	@Test
	void GetAllSeatQueryHandler_handle_ReturnListSeat() {
		
		seatList.add(seatTest);
		Mockito.when(genericNoSqlReadRepositoryPort.findAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(seatList);
		
		List<Seat> list = getAllSeatQueryHandler.handle(getAllSeatQuery);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getSeatId()).isEqualTo(seatId),
				()-> Assertions.assertThat(list.get(0).getSeatNumber()).isEqualTo(seatNumber),
				()-> Assertions.assertThat(list.get(0).getPlane()).isEqualTo(planeTest)
				);
	}	

	@Test
	void GetAllSeatQueryHandler_handle_ReturnIllegalArgumentException() {		
				
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> getAllSeatQueryHandler.handle(getByIdSeatQuery));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}	

}
