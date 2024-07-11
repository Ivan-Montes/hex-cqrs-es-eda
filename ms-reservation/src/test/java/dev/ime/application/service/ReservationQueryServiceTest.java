package dev.ime.application.service;


import java.util.ArrayList;
import java.util.List;
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

import dev.ime.application.dispatch.ReservationQueryDispatcher;
import dev.ime.domain.model.Reservation;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@ExtendWith(MockitoExtension.class)
class ReservationQueryServiceTest {

	@Mock
	private ReservationQueryDispatcher reservationQueryDispatcher;

	@InjectMocks
	private ReservationQueryService reservationQueryService;

	@Mock
	private QueryHandler<Object> queryHandler;

	private final UUID reservationId = UUID.randomUUID();
	private final UUID clientId = UUID.randomUUID();
	private final UUID flightId = UUID.randomUUID();
	private Set<UUID> seatIdsSet;
	private Reservation reservationTest;
	private List<Reservation> reservationList;

	@BeforeEach
	private void createObjects() {
		
		reservationList = new ArrayList<>();
		reservationTest = new Reservation(reservationId, clientId, flightId, seatIdsSet);
		
	}
	
	@Test
	void ReservationQueryService_getAll_ReturnListReservation() {
		
		reservationList.add(reservationTest);
		Mockito.when(reservationQueryDispatcher.getQueryHandler(Mockito.any(Query.class))).thenReturn(queryHandler);
		Mockito.when(queryHandler.handle(Mockito.any(Query.class))).thenReturn(reservationList);
		
		List<Reservation> list = reservationQueryService.getAll(0,1);
		
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
	void ReservationQueryService_getById_ReturnOptionalReservation() {

		Mockito.when(reservationQueryDispatcher.getQueryHandler(Mockito.any(Query.class))).thenReturn(queryHandler);
		Mockito.when(queryHandler.handle(Mockito.any(Query.class))).thenReturn(Optional.ofNullable(reservationTest));
		
		Optional<Reservation> optReservation = reservationQueryService.getById(reservationId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optReservation).isNotNull(),
				()-> Assertions.assertThat(optReservation.get().getReservationId()).isEqualTo(reservationId),
				()-> Assertions.assertThat(optReservation.get().getClientId()).isEqualTo(clientId),
				()-> Assertions.assertThat(optReservation.get().getFlightId()).isEqualTo(flightId),
				()-> Assertions.assertThat(optReservation.get().getSeatIdsSet()).isEqualTo(seatIdsSet),
				()-> Assertions.assertThat(optReservation.get().toString()).hasToString(reservationTest.toString()),
				()-> Assertions.assertThat(optReservation.get().hashCode()).hasSameHashCodeAs(reservationTest.hashCode()),
				()-> Assertions.assertThat(optReservation).contains(reservationTest)
				);	
	}
	
}
