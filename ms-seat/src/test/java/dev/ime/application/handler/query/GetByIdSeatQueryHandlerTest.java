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
import dev.ime.application.usecase.query.GetAllSeatQuery;
import dev.ime.application.usecase.query.GetByIdSeatQuery;
import dev.ime.domain.model.Plane;
import dev.ime.domain.model.Seat;
import dev.ime.domain.port.outbound.GenericNoSqlReadRepositoryPort;

@ExtendWith(MockitoExtension.class)
class GetByIdSeatQueryHandlerTest {

	@Mock
	private GenericNoSqlReadRepositoryPort<Seat> genericNoSqlReadRepositoryPort;

	@InjectMocks
	private GetByIdSeatQueryHandler getByIdSeatQueryHandler;	

	private GetAllSeatQuery getAllSeatQuery;
	private GetByIdSeatQuery getByIdSeatQuery;
	private Seat seatTest;
	private Plane planeTest;
	private final UUID seatId = UUID.randomUUID();
	private final String seatNumber = "PS1973";
	private final UUID planeId = UUID.randomUUID();

	@BeforeEach
	private void createObjects() {
		
		getAllSeatQuery = new GetAllSeatQuery(0, 1);
		getByIdSeatQuery = new GetByIdSeatQuery(seatId);		
	
		planeTest = new Plane();
		planeTest.setPlaneId(planeId);
		
		seatTest = new Seat();
		seatTest.setSeatId(seatId);
		seatTest.setSeatNumber(seatNumber);
		seatTest.setPlane(planeTest);
	}
	
	@Test
	void GetByIdSeatQueryHandler_handle_ReturnOptionalSeat() {
		
		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(seatTest));

		Optional<Seat> optSeat = getByIdSeatQueryHandler.handle(getByIdSeatQuery);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optSeat).isNotNull(),
				()-> Assertions.assertThat(optSeat.get().getSeatId()).isEqualTo(seatId),
				()-> Assertions.assertThat(optSeat.get().getSeatNumber()).isEqualTo(seatNumber),
				()-> Assertions.assertThat(optSeat.get().getPlane()).isEqualTo(planeTest)
				);
		
	}

	@Test
	void GetAllSeatQueryHandler_handle_ReturnIllegalArgumentException() {		
				
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> getByIdSeatQueryHandler.handle(getAllSeatQuery));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}	

	@Test
	void GetByIdSeatQueryHandler_handle_ReturnResourceNotFoundException() {
		
		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
	
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> getByIdSeatQueryHandler.handle(getByIdSeatQuery));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}
	
}
