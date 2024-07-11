package dev.ime.application.handler;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.usecase.GetAllReservationQuery;
import dev.ime.application.usecase.GetByIdReservationQuery;
import dev.ime.domain.model.Reservation;
import dev.ime.domain.port.outbound.GenericReadRepositoryPort;

@ExtendWith(MockitoExtension.class)
class GetAllReservationQueryHandlerTest {

	@Mock
	private GenericReadRepositoryPort<Reservation> genericNoSqlReadRepositoryPort;

	@InjectMocks
	private GetAllReservationQueryHandler getAllReservationQueryHandler;

	private final UUID reservationId = UUID.randomUUID();
	private final UUID clientId = UUID.randomUUID();
	private final UUID flightId = UUID.randomUUID();
	private Set<UUID> seatIdsSet;
	private UUID seatId = UUID.randomUUID();
	private List<Reservation> reservationList;
	private Reservation reservationTest;
	private GetAllReservationQuery getAllReservationQuery;
	private GetByIdReservationQuery getByIdReservationQuery;
	
	@BeforeEach
	private void createObjects() {
		
		seatIdsSet = new HashSet<>();
		reservationList = new ArrayList<>();
		
		reservationTest = new Reservation();
		reservationTest.setReservationId(reservationId);
		reservationTest.setClientId(clientId);
		reservationTest.setFlightId(flightId);
		reservationTest.setSeatIdsSet(seatIdsSet);
		
		getAllReservationQuery = new GetAllReservationQuery(0,1);
		getByIdReservationQuery = new GetByIdReservationQuery(reservationId);
		
	}

	@Test
	void GetAllReservationQueryHandler_handle_ReturnList() {
		
		seatIdsSet.add(seatId);
		reservationList.add(reservationTest);
		Mockito.when(genericNoSqlReadRepositoryPort.findAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(reservationList);
		
		List<Reservation> list = getAllReservationQueryHandler.handle(getAllReservationQuery);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getReservationId()).isEqualTo(reservationId),
				()-> Assertions.assertThat(list.get(0).getClientId()).isEqualTo(clientId),
				()-> Assertions.assertThat(list.get(0).getFlightId()).isEqualTo(flightId),
				()-> Assertions.assertThat(list.get(0).getSeatIdsSet()).isEqualTo(seatIdsSet)
				);		
	}

	@Test
	void GetAllReservationQueryHandler_handle_ReturnIllegalArgumentException() {		
				
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> getAllReservationQueryHandler.handle(getByIdReservationQuery));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}

}
