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

import dev.ime.application.dispatch.SeatQueryDispatcher;
import dev.ime.domain.model.Plane;
import dev.ime.domain.model.Seat;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@ExtendWith(MockitoExtension.class)
class SeatQueryServiceTest {

	@Mock
	private SeatQueryDispatcher seatQueryDispatcher;

	@InjectMocks
	private SeatQueryService seatQueryService;

	@Mock
	private QueryHandler<Object> queryHandler;
	
	private List<Seat> seatList;
	private Seat seatTest;
	private Plane planeTest;
	private final UUID seatId = UUID.randomUUID();
	private final String seatNumber = "PS1973";
	private final UUID planeId = UUID.randomUUID();

	@BeforeEach
	private void createObjects() {
		
		seatList = new ArrayList<>();
		
		planeTest = new Plane();
		planeTest.setPlaneId(planeId);
		
		seatTest = new Seat(
				seatId,
				seatNumber,
				planeTest);
		
	}	

	@Test
	void SeatQueryService_getAll_ReturnListSeat() {
		
		seatList.add(seatTest);
		Mockito.when(seatQueryDispatcher.getQueryHandler(Mockito.any(Query.class))).thenReturn(queryHandler);
		Mockito.when(queryHandler.handle(Mockito.any(Query.class))).thenReturn(seatList);
		
		List<Seat> list = seatQueryService.getAll(0,1);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getSeatId()).isEqualTo(seatId),
				()-> Assertions.assertThat(list.get(0).getSeatNumber()).isEqualTo(seatNumber),
				()-> Assertions.assertThat(list.get(0).getPlane()).isEqualTo(planeTest)
				);
	}

	@Test
	void SeatQueryService_getById_ReturnOptionalSeat() {
		
		Mockito.when(seatQueryDispatcher.getQueryHandler(Mockito.any(Query.class))).thenReturn(queryHandler);
		Mockito.when(queryHandler.handle(Mockito.any(Query.class))).thenReturn(Optional.ofNullable(seatTest));
	
		Optional<Seat> optSeat = seatQueryService.getById(seatId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optSeat).isNotNull(),
				()-> Assertions.assertThat(optSeat.get().getSeatId()).isEqualTo(seatId),
				()-> Assertions.assertThat(optSeat.get().getSeatNumber()).isEqualTo(seatNumber),
				()-> Assertions.assertThat(optSeat.get().getPlane()).isEqualTo(planeTest),
				()-> Assertions.assertThat(optSeat.get().toString()).hasToString(seatTest.toString()),
				()-> Assertions.assertThat(optSeat.get().hashCode()).hasSameHashCodeAs(seatTest.hashCode()),
				()-> Assertions.assertThat(optSeat).contains(seatTest)
				);		
	}

}
