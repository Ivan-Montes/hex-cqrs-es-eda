package dev.ime.application.service;


import java.time.LocalDate;
import java.time.LocalTime;
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

import dev.ime.application.dispatch.FlightQueryDispatcher;
import dev.ime.domain.model.Flight;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@ExtendWith(MockitoExtension.class)
class FlightQueryServiceTest {

	@Mock
	private FlightQueryDispatcher flightQueryDispatcher;
	
	@InjectMocks
	private FlightQueryService flightQueryService;

	@Mock
	private QueryHandler<Object> queryHandler;
	
	private List<Flight> flightList;
	private Flight flightTest;
	private final UUID flightId = UUID.randomUUID();
	private final String origin = "Madrid";
	private final String destiny = "Tokyo";
	private final LocalDate departureDate = LocalDate.parse("2024-04-01");
	private final LocalTime departureTime = LocalTime.parse("15:00");
	private final UUID planeId = UUID.randomUUID();
	
	@BeforeEach
	private void createObjects() {
		
		flightList = new ArrayList<>();
		
		flightTest = new Flight(
				flightId,
				origin,
				destiny,
				departureDate,
				departureTime,
				planeId);
	}
		
	@Test
	void FlightQueryService_getAll_ReturnListFlight() {
		
		flightList.add(flightTest);
		Mockito.when(flightQueryDispatcher.getQueryHandler(Mockito.any(Query.class))).thenReturn(queryHandler);
		Mockito.when(queryHandler.handle(Mockito.any(Query.class))).thenReturn(flightList);
		
		List<Flight> list = flightQueryService.getAll();
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getFlightId()).isEqualTo(flightId),
				()-> Assertions.assertThat(list.get(0).getOrigin()).isEqualTo(origin),
				()-> Assertions.assertThat(list.get(0).getDestiny()).isEqualTo(destiny),
				()-> Assertions.assertThat(list.get(0).getDepartureDate()).isEqualTo(departureDate),
				()-> Assertions.assertThat(list.get(0).getDepartureTime()).isEqualTo(departureTime),
				()-> Assertions.assertThat(list.get(0).getPlaneId()).isEqualTo(planeId)
				);	
	}
	
	@Test
	void FlightQueryService_getById_ReturnOptionalFlight() {
		
		Mockito.when(flightQueryDispatcher.getQueryHandler(Mockito.any(Query.class))).thenReturn(queryHandler);
		Mockito.when(queryHandler.handle(Mockito.any(Query.class))).thenReturn(Optional.ofNullable(flightTest));
		
		Optional<Flight> optFlight = flightQueryService.getById(flightId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optFlight).isNotNull(),
				()-> Assertions.assertThat(optFlight.get().getFlightId()).isEqualTo(flightId),
				()-> Assertions.assertThat(optFlight.get().getOrigin()).isEqualTo(origin),
				()-> Assertions.assertThat(optFlight.get().getDestiny()).isEqualTo(destiny),
				()-> Assertions.assertThat(optFlight.get().getDepartureDate()).isEqualTo(departureDate),
				()-> Assertions.assertThat(optFlight.get().getDepartureTime()).isEqualTo(departureTime),
				()-> Assertions.assertThat(optFlight.get().getPlaneId()).isEqualTo(planeId),
				()-> Assertions.assertThat(optFlight.get().toString()).hasToString(flightTest.toString()),
				()-> Assertions.assertThat(optFlight.get().hashCode()).hasSameHashCodeAs(flightTest.hashCode()),
				()-> Assertions.assertThat(optFlight).contains(flightTest)
				);	
	}
	

}
