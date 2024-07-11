package dev.ime.application.handler;


import java.util.HashSet;
import java.util.Optional;
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

import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.GetAllReservationQuery;
import dev.ime.application.usecase.GetByIdReservationQuery;
import dev.ime.domain.model.Reservation;
import dev.ime.domain.port.outbound.GenericReadRepositoryPort;

@ExtendWith(MockitoExtension.class)
class GetByIdReservationQueryHandlerTest {

	@Mock
	private GenericReadRepositoryPort<Reservation> genericNoSqlReadRepositoryPort;

	@InjectMocks
	private GetByIdReservationQueryHandler getByIdReservationQueryHandler;
	
	private final UUID reservationId = UUID.randomUUID();
	private final UUID clientId = UUID.randomUUID();
	private final UUID flightId = UUID.randomUUID();
	private Set<UUID> seatIdsSet;
	private Reservation reservationTest;
	private GetAllReservationQuery getAllReservationQuery;
	private GetByIdReservationQuery getByIdReservationQuery;
	
	@BeforeEach
	private void createObjects() {
		
		seatIdsSet = new HashSet<>();
		
		reservationTest = new Reservation();
		reservationTest.setReservationId(reservationId);
		reservationTest.setClientId(clientId);
		reservationTest.setFlightId(flightId);
		reservationTest.setSeatIdsSet(seatIdsSet);
		
		getAllReservationQuery = new GetAllReservationQuery(0,1);
		getByIdReservationQuery = new GetByIdReservationQuery(reservationId);
		
	}

	@Test
	void GetByIdReservationQueryHandler_handle_ReturnOpt() {
		
		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(reservationTest));
		
		Optional<Reservation> optReservation = getByIdReservationQueryHandler.handle(getByIdReservationQuery);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optReservation).isNotNull(),
				()-> Assertions.assertThat(optReservation.get().getReservationId()).isEqualTo(reservationId),
				()-> Assertions.assertThat(optReservation.get().getClientId()).isEqualTo(clientId),
				()-> Assertions.assertThat(optReservation.get().getFlightId()).isEqualTo(flightId),
				()-> Assertions.assertThat(optReservation.get().getSeatIdsSet()).isEqualTo(seatIdsSet)
				);		
	}

	@Test
	void GetByIdReservationQueryHandler_handle_ReturnIllegalArgumentException() {		
				
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> getByIdReservationQueryHandler.handle(getAllReservationQuery));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}

	@Test
	void GetByIdReservationQueryHandler_handle_ReturnResourceNotFoundException() {
		
		Mockito.when(genericNoSqlReadRepositoryPort.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
	
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()-> getByIdReservationQueryHandler.handle(getByIdReservationQuery));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(ResourceNotFoundException.class)
				);		
	}	

}
